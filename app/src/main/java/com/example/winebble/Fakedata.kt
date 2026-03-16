package com.example.winebble

import androidx.compose.runtime.Composable

/**
 * Project: Winebble
 * From: com.example.winebble
 * Created by: Joel Arturo Osorio
 * On: 04/03/2026 at 17: 30
 * All rights reserved 2026.
 */

@Composable
fun getProfileItems(): List<Pair<String, Int>> = listOf(
    "Cuenta" to R.drawable.icon_person,
    "Historial de pedidos" to R.drawable.orders_icon,
    "Codigos promocionales" to R.drawable.code_icon,
    "Notificaciones" to R.drawable.notifications_icon,
    "Preguntas frecuentes" to R.drawable.question_icon,
    "Cerrar sesión" to R.drawable.logout_icon)
fun getAllWine() = listOf(
    Wine("Banyuls Traditionnel 1949, L'Etoile",
        "Nacido en las escarpadas laderas de esquisto con vistas al Mediterráneo, es fruto del trabajo heroico de los viticultores de la Cave de l'Étoile, una finca pionera fundada en 1921. Una auténtica pieza de coleccionista, esta botella encarna la historia viva del Rosellón y la nobleza atemporal de los vinos dulces naturales añejos.",
        "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcS0XWLK2-xlOa7DbDshDguB28a5Nnx5mYw9k-lzMYSkHhgjZaeuRhR7AqAoAfNxsdTfclXEdoKaeHaFb-oL0UwpE5VLj1TFx1wZZlYVZwjCJuoPUspJiuBs&usqp=CAc",
        "Origen: francés",
        835.0),
    Wine("Vino tinto Pétrus 2020 Pomerol",
        "Los vinos de Petrus parecen merecer todos los superlativos que les atribuyen y la devoción casi mística de los aficionados al vino. Se trata de un vino único, el más concentrado y rico del Pomerol.",
        "https://cdn.grupoelcorteingles.es/SGFM/dctm/MEDIA03/202403/07/00113351505667____2__1200x1200.jpg?impolicy=Resize&width=1200",
        "Origen: frances",
        510.0),
    Wine("Vega Sicilia Único",
        "Generaciones que han labrado una historia de ilusión y compromiso, el mejor vino siempre está por hacer ",
        "https://m.media-amazon.com/images/I/41vHoq4nUPL._AC_SY741_.jpg",
        "Origen: italiano",
        220.0),
    Wine("Château Lafite Rothschild 2009",
        "Un Lafite asombroso, que conserva la elegancia clásica de esta icónica bodega, resultado de una cosecha de climatología muy favorable y de una severa selección de uvas de las variedades Cabernet Sauvignon, Merlot y Petit Verdot.",
        "https://bodegadirecta.es/535-large_default/chateau-lafite-rothschild-2009.jpg",
        "Origen: francés",
        120.0),
    Wine("Chateau Lafite Rothschild 1938",
        "Para el coleccionista exigente, el Château Lafite-Rothschild 1938 es una joya excepcional que encapsula la esencia de una cosecha histórica de Burdeos. Como parte de la prestigiosa serie Lafite-Rothschild, este vino representa no solo una calidad excepcional, sino también una época significativa en el arte de la vinificación",
        "https://wein-sammeln.de/cdn/shop/files/chateau-lafite-rothschild-1938-0.jpg?v=1749467085&width=823 ",
        "Origen: frances",
        110.0),
    Wine("Pingus, Ribera del Duero",
        "El vino más caro de España vio la luz en 1995 y desde entonces no ha dejado de cosechar alabanzas y elogios por parte de toda la crítica internacional especializada. ",
        "https://cdn.vinissimus.com/img/unsafe/keep/plain/local:///landings/9cb2c1e7-fced-4208-8c4c-84e6abd69d3c/dpin18-anv300_c771075d.png",
        "Origen: español",
        80.0)
)