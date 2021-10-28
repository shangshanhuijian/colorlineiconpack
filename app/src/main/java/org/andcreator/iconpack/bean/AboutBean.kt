package org.andcreator.iconpack.bean

data class AboutBean(val title: String,
                     val content: String,
                     val photo: Int,
                     val banner: Int,
                     val buttons: ArrayList<Int>,
                     val links: ArrayList<String>)