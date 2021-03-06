package com.thelastpickle.tlpcluster.configuration

import org.apache.logging.log4j.kotlin.logger

data class Host(val public: String,
                val private: String,
                val alias: String) {

    companion object {
        val hostRegex = """aws_instance\.(\w+)(.(\d+))?""".toRegex()
        val log = logger()

        fun fromTerraformString(str: String, public: String, private: String) : Host {
            val tmp = hostRegex.find(str)!!.groups

            val serverType = tmp[1]?.value.toString()
            val serverNum = (tmp[3]?.value ?: 0).toString()

            log.debug { "Regex find: $tmp" }
            return Host(public, private, serverType + serverNum)

        }
    }
}

