package com.vitorblog.studentplusserver.model

enum class Point(val url: String){

    GET_STUDENT_INFO("http://webservices.sed.sc.gov.br/eol2/rest/wsConsultaEscolaAluno"),
    GET_STUDENT_REPORT("http://webservices.sed.sc.gov.br/eol2/rest/wsConsultaBoletim"),
    GET_STUDENT_SUBJECTS("http://webservices.sed.sc.gov.br/eol2/rest/wsConsultaGradeAluno"),
    GET_STUDENT_GRADES("http://webservices.sed.sc.gov.br/eol2/rest/wsConsultaNotasDisci"),

    GET_SCHOOL_MESSAGES("http://webservices.sed.sc.gov.br/eol2/rest/wsConsultaNotasDisci"),
    GET_SCHOOL_SCHEDULE("http://webservices.sed.sc.gov.br/eol2/rest/wsConsultaAgendaAluno"),
    GET_SCHOOL_MENU("http://webservices.sed.sc.gov.br/eol2/rest/wsConsultaCardapio"),

}
