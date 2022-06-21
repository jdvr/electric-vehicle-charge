package es.juandavidvega.ecc.cli

import kotlinx.cli.*


object App {
    const val appName = "ECC Client CLI"
    const val version = "0.0.1"
}

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val parser = ArgParser("${App.appName}:: ${App.version}")
    val version by parser.option(ArgType.Boolean, shortName = "V", description = "Version").default(false)

    // Add all input to parser
    parser.parse(args)

    if(version) println(App.version)
}
