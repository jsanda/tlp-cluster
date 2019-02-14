package com.thelastpickle.tlpcluster.commands

import java.io.File

class Clean : ICommand {
    override fun execute() {
        File("seeds.txt").delete()
        File("terraform.tfstate").delete()
        File("terraform.tfstate.backup").delete()
        File("stress_ips.txt").delete()
        File("hosts.txt").delete()
        File("terraform.tf.json").delete()
        File("terraform.tfvars").delete()
        File("build_cassandra.sh").delete()

        val toDelete = listOf("copy_provisioning_resources.sh",
                "create_provisioning_resources.sh",
                "docker-compose.yml",
                "Dockerfile",
                "DockerfileCassandra",
                "DockerfileSSH"
                )

        for(f in toDelete) {
            File(f).delete()
        }
        File(".terraform").deleteRecursively()
    }

}