package com.example.myvetapp

data class Disease(var dNum: Int, var dName: String, var dKinds: String, var dDef: String, var dCause: String, var dPath: String, var dTrouble: String, var dSymptom: String, var dDiagnosis: String, var dDifference: String, var dPathology: String,  var dCure: String, var dPrevention: String, var dPrognosis: String,  var dAdvice: String, var dEtc: String, var dReference: String) {
    constructor():this(0,"NoInfo", "NoInfo","NoInfo","NoInfo","NoInfo", "NoInfo", "NoInfo", "NoInfo", "NoInfo", "NoInfo", "NoInfo", "NoInfo", "NoInfo", "NoInfo", "NoInfo", "NoInfo")
}