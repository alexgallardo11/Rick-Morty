package com.example.rickmorty.domain.enums

object CharacterFilters {
    enum class Status(val label: String) { Alive("Alive"), Dead("Dead"), Unknown("unknown") }
    enum class Gender(val label: String) { Male("Male"), Female("Female"), Genderless("Genderless"), Unknown("unknown") }
    enum class Species(val label: String) {
        Human("Human"), Alien("Alien"), Robot("Robot"), Animal("Animal"), MythologicalCreature("Mythological Creature"), Unknown("unknown")
    }
}