package it.dellapp.calcettomazzano

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform