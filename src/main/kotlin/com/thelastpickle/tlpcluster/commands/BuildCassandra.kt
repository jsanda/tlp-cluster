package com.thelastpickle.tlpcluster.commands

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.thelastpickle.tlpcluster.Cassandra
import com.thelastpickle.tlpcluster.Context
import com.thelastpickle.tlpcluster.containers.CassandraBuildJava8
import java.io.File

@Parameters(commandDescription = "Build Cassandra (either tag or custom dir)")
class BuildCassandra(val context: Context)  : ICommand {

    @Parameter(description = "build_name [path | tag]")
    var params: List<String> = mutableListOf()

    override fun execute() {

        val name = params[0]
        val pathOrVersion = params[1]

        val tmp = File(pathOrVersion)

        // does the build already exist?  if so, bail out


        // is this a directory?
        val location = if(tmp.exists()) {
            tmp
        } else {
            // not a dir, must be a tag
            println("Directory not found, attempting to build ref $pathOrVersion")
            context.cassandraRepo.checkoutVersion(pathOrVersion)
            context.cassandraRepo.gitLocation
        }


        context.createBuildSkeleton(name)

        val cassandra = CassandraBuildJava8(context)

        // create the container
        cassandra.build()
        cassandra.start(location.absolutePath, name)

        /*
                val dc = DockerCompose(inheritIO = true)

            return dc
                    .setBuildName(name)
                    .setCassandraDir(location.absolutePath)
                    .run("build-cassandra", arrayOf())
         */
//        Cassandra.build(name, location)
    }
}