package com.example.pusika.scrum.model

class ResultOfRound(
        private val hits: Int,
        private val misses: Int,
        private val blocks: Int,
        private val cuts: Int,
        private val heroHp: Int,
        private val enemyHp: Int
) {

    override fun toString(): String {
        return "Результаты раунда: сделано ударов=$hits, промахов=$misses," +
                " заблокировано ударов=$blocks, получено ранений=$cuts, " +
                "здоровье героя=$heroHp, здоровье противника=$enemyHp)"
    }
}
