package com.vitorblog.studentplusserver.process

import com.fasterxml.jackson.databind.ObjectMapper
import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlTextInput
import com.vitorblog.studentplusserver.model.*
import com.vitorblog.studentplusserver.requester.APIRequester
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.jsoup.Jsoup
import java.text.SimpleDateFormat

class SubjectProcess {
    val jsonParser = JSONParser()
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    val simpleDateFormat2 = SimpleDateFormat("yyyy-MM-dd")

    fun load(id:String, birthdate:String):ArrayList<Subject>{
        val subjects = arrayListOf<Subject>()

        val subjectListResponse = APIRequester.request(Point.GET_STUDENT_SUBJECTS, "{\"AlunoCod\": $id}")
        val subjectListJSON = jsonParser.parseArray(jsonParser.parseJSON(subjectListResponse).getString("ResultadoAluno")).first() as JSONObject

        val teachers = getTeachers(id, birthdate)

        for (sub in subjectListJSON.getArray("dsiciplinas")){
            val subject = sub as JSONObject
            val name = subject.getString("Nm").formatName()

            val grades = arrayListOf<Grade>()
            val gradesJSON = jsonParser.parseJSON(jsonParser.parseJSON(APIRequester.request(Point.GET_STUDENT_GRADES, "{\"AlunoCod\":$id,\"DisciCod\":\"${subject.getString("Cd")}\",\"turmaParm\":\"${subjectListJSON.getString("Turma")}\"}")).getString("ResultadoNotas"))
            if (gradesJSON.get("AlunoSemNota")!!.equals(false)){
                for (grade2 in gradesJSON.getArray("NotaWS")){
                    val grade = grade2 as JSONObject

                    grades.add(Grade(grade.getString("AtividadesCod"), grade.getString("TiposAtividadeDescricao"), grade.getInt("AtividadesBimestre"), grade.get("AtividadeNota") as Double, grade.get("AtividadeMediaTurma") as Double))
                }
            }

            try {
                subjects.add(Subject(subject.getString("Cd"), name, teachers.get(subject.getString("Nm").split("  ")[0])!!, grades))
            }catch (e:Exception){
                subjects.add(Subject(subject.getString("Cd"), name, "", grades))
            }
        }

        return subjects
    }

    fun getTeachers(id:String, birthdate:String):HashMap<String, String>{
        val teachers = hashMapOf<String, String>()

        val webClient = WebClient(BrowserVersion.FIREFOX_60)
        webClient.options.isThrowExceptionOnScriptError = false

        /* Login */
        var page = webClient.getPage<HtmlPage>("http://scescolaaluno.ciasc.gov.br/cadploginaluno.aspx")

        val form = page.getFormByName("MAINFORM")

        val studentId = form.getInputByName<HtmlTextInput>("vALUNOCOD")
        studentId.type(id)
        val studentBirth = form.getInputByName<HtmlTextInput>("vALUNODATNAS")
        studentBirth.type(simpleDateFormat.format(simpleDateFormat2.parse(birthdate)))

        val button = form.getInputByValue<HtmlButtonInput>("Entrar")
        button.setAttribute("type", "submit")

        button.click<HtmlPage>()

        /* Get Teacher */
        page = webClient.getPage("http://scescolaaluno.ciasc.gov.br/cadmeusprofessores.aspx")
        val document = Jsoup.parse(page.asXml())
        for (i in listOf("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20")){
            try {
                teachers.put(document.getElementById("span_vDISCIPLINANOM_00$i").text().substring(6), document.getElementById("span_vPESNOM_00$i").text())
            }catch (e:Exception){
                webClient.close()
                return teachers
            }
        }

        webClient.close()
        return teachers
    }

    fun String.formatName():String {
        val split = this.split(" ")
        var fullName = ""

        for (name in split){
            if (name.length > 0){
                fullName += " ${name.substring(0, 1).toUpperCase()}${name.substring(1).toLowerCase()}"
            }
        }

        return fullName.substring(1).split("  ")[0]
    }
}