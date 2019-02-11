package com.example.pusika.scrum.model

import java.io.Serializable

class Status : Serializable {

    var name: String? = null
    var value: Int = 0
    var description: String? = null
    var isVisible: Boolean = false

    constructor()

    constructor(name: String, value: Int) {
        this.name = name
        this.value = value
    }

    constructor(name: String, value: Int, description: String, isVisible: Boolean) {
        this.name = name
        this.value = value
        this.description = description
        this.isVisible = isVisible
    }
}
