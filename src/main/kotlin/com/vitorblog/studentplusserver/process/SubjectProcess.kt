package com.vitorblog.studentplusserver.process

import com.fasterxml.jackson.databind.ObjectMapper
import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlTextInput
import com.vitorblog.studentplusserver.model.*
import com.vitorblog.studentplusserver.requester.APIRequester
import com.vitorblog.studentplusserver.utils.StringUtils
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.jsoup.Jsoup
import java.text.SimpleDateFormat

class SubjectProcess {
    private val jsonParser = JSONParser()
    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private val simpleDateFormat2 = SimpleDateFormat("yyyy-MM-dd")

    fun load(id:String, birthday:String):ArrayList<Subject>{
        val subjects = arrayListOf<Subject>()

        val subjectListResponse = APIRequester.request(Point.GET_STUDENT_SUBJECTS, "{\"AlunoCod\": $id}")
        val subjectListJSON = jsonParser.parseArray(jsonParser.parseJSON(subjectListResponse).getString("ResultadoAluno")).first() as JSONObject

        val teachers = getTeachers(id, birthday)

        for (sub in subjectListJSON.getArray("dsiciplinas")){
            val subject = sub as JSONObject
            val name = StringUtils.formatName(subject.getString("Nm"))

            val grades = arrayListOf<Grade>()
            val gradesJSON = jsonParser.parseJSON(jsonParser.parseJSON(APIRequester.request(Point.GET_STUDENT_GRADES, "{\"AlunoCod\":$id,\"DisciCod\":\"${subject.getString("Cd")}\",\"turmaParm\":\"${subjectListJSON.getString("Turma")}\"}")).getString("ResultadoNotas"))
            if (gradesJSON["AlunoSemNota"]!! == false){
                for (grade2 in gradesJSON.getArray("NotaWS")){
                    val grade = grade2 as JSONObject

                    grades.add(Grade(grade.getString("AtividadesCod"), grade.getString("TiposAtividadeDescricao"), grade.getInt("AtividadesBimestre"), grade["AtividadeNota"] as Double, grade["AtividadeMediaTurma"] as Double))
                }
            }

            try {
                subjects.add(Subject(subject.getString("Cd"), name, teachers[subject.getString("Nm").split("  ")[0]]!!, grades))
            }catch (e:Exception){
                subjects.add(Subject(subject.getString("Cd"), name, "", grades))
            }
        }

        return subjects
    }

    private fun getTeachers(id:String, birthday:String):HashMap<String, String>{
        val teachers = hashMapOf<String, String>()

        val webClient = WebClient(BrowserVersion.FIREFOX_60)
        webClient.options.isThrowExceptionOnScriptError = false

        /* Login */
        var page = webClient.getPage<HtmlPage>("http://scescolaaluno.ciasc.gov.br/cadploginaluno.aspx")

        val form = page.getFormByName("MAINFORM")

        val studentId = form.getInputByName<HtmlTextInput>("vALUNOCOD")
        studentId.type(id)
        val studentBirth = form.getInputByName<HtmlTextInput>("vALUNODATNAS")
        studentBirth.type(simpleDateFormat.format(simpleDateFormat2.parse(birthday)))

        val button = form.getInputByValue<HtmlButtonInput>("Entrar")
        button.setAttribute("type", "submit")

        button.click<HtmlPage>()

        /* Get Teacher */
        page = webClient.getPage("http://scescolaaluno.ciasc.gov.br/cadmeusprofessores.aspx")
        val document = Jsoup.parse(page.asXml())
        for (i in listOf("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20")){
            try {
                teachers[document.getElementById("span_vDISCIPLINANOM_00$i").text().substring(6)] =
                    StringUtils.formatName(document.getElementById("span_vPESNOM_00$i").text()).replace("--- ---", "Sem professor no sistema")
            }catch (e:Exception){
                webClient.close()
                return teachers
            }
        }

        webClient.close()
        return teachers
    }

}