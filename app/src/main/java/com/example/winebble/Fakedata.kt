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
fun getProfileItems(): List<ProfileData> = listOf(
    ProfileData(
        title = "Configuracion de la cuenta",
        icon = R.drawable.setting_icon,
        route = "configuracion"),
    ProfileData(
        title = "Historial de pedidos",
        icon = R.drawable.orders_icon,
        route = "historial"),
    ProfileData(
        title = "Metodos de pago",
        icon = R.drawable.payment_icon,
        route = "metodos"),
    ProfileData(
        title = "Codigos promocionales",
        icon = R.drawable.code_icon,
        route = "codigos promocionales"),
    ProfileData(
        title = "Notificaciones",
        icon = R.drawable.notifications_icon,
        route = "notificaciones"),
    ProfileData(
        title = "Atencion al cliente",
        icon = R.drawable.customer_icon,
        route = "atencion"),
    ProfileData(
        title = "Preguntas frecuentes",
        icon = R.drawable.question_icon,
        route = "preguntas frecuentes"),
    ProfileData(
        title = "Cerrar sesion",
        icon = R.drawable.logout_icon,
        route = "cerrar sesion")
)

fun getAllWine() = listOf(
    WineData("Banyuls Traditionnel 1949, L'Etoile",
        "Nacido en las escarpadas laderas de esquisto con vistas al Mediterraneo, es fruto del trabajo heroico de los viticultores de la Cave de l'Etoile, una finca pionera fundada en 1921. Una autentica pieza de coleccionista, esta botella encarna la historia viva del Rosellon y la nobleza atemporal de los vinos dulces naturales anejos.",
        "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcS0XWLK2-xlOa7DbDshDguB28a5Nnx5mYw9k-lzMYSkHhgjZaeuRhR7AqAoAfNxsdTfclXEdoKaeHaFb-oL0UwpE5VLj1TFx1wZZlYVZwjCJuoPUspJiuBs&usqp=CAc",
        "Origen: frances",
        835.0),
    WineData("Vino tinto Petrus 2020 Pomerol",
        "Los vinos de Petrus parecen merecer todos los superlativos que les atribuyen y la devocion casi mistica de los aficionados al vino. Se trata de un vino unico, el mas concentrado y rico del Pomerol.",
        "https://cdn.grupoelcorteingles.es/SGFM/dctm/MEDIA03/202403/07/00113351505667____2__1200x1200.jpg?impolicy=Resize&width=1200",
        "Origen: frances",
        510.0),
    WineData("Vega Sicilia Unico",
        "Generaciones que han labrado una historia de ilusion y compromiso, el mejor vino siempre esta por hacer ",
        "https://m.media-amazon.com/images/I/41vHoq4nUPL._AC_SY741_.jpg",
        "Origen: italiano",
        220.0),
    WineData("Chateau Lafite Rothschild 2009",
        "Un Lafite asombroso, que conserva la elegancia clasica de esta iconica bodega, resultado de una cosecha de climatologia muy favorable y de una severa seleccion de uvas de las variedades Cabernet Sauvignon, Merlot y Petit Verdot.",
        "https://bodegadirecta.es/535-large_default/chateau-lafite-rothschild-2009.jpg",
        "Origen: frances",
        120.0),
    WineData("Chateau Lafite Rothschild 1938",
        "Para el coleccionista exigente, el Chateau Lafite-Rothschild 1938 es una joya excepcional que encapsula la esencia de una cosecha historica de Burdeos. Como parte de la prestigiosa serie Lafite-Rothschild, este vino representa no solo una calidad excepcional, sino tambien una epoca significativa en el arte de la vinificacion",
        "https://wein-sammeln.de/cdn/shop/files/chateau-lafite-rothschild-1938-0.jpg?v=1749467085&width=823 ",
        "Origen: frances",
        110.0),
    WineData("Pingus, Ribera del Duero",
        "El vino mas caro de Espana vio la luz en 1995 y desde entonces no ha dejado de cosechar alabanzas y elogios por parte de toda la critica internacional especializada. ",
        "https://cdn.vinissimus.com/img/unsafe/keep/plain/local:///landings/9cb2c1e7-fced-4208-8c4c-84e6abd69d3c/dpin18-anv300_c771075d.png",
        "Origen: espanol",
        80.0)
)

fun getAllLicor() = listOf(
    LicorData("Vodka Belvedere",
        "Vodka premium elaborado en Polonia con centeno Dankowskie y agua purificada de pozos artesanales. Destaca por su textura cremosa, notas de vainilla y un final suave con especias blancas.",
        "https://cdn.grupoelcorteingles.es/SGFM/dctm/MEDIA03/202011/25/00113357600124____29__1200x1200.jpg",
        "Origen: polaco",
        45.29),
    LicorData("Vodka Grey Goose",
        "Vodka frances elaborado con trigo de alta calidad y agua de manantial de Cognac. Tiene un perfil limpio y elegante, con una entrada sedosa pensada para tomar sola o en coctel.",
        "https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/202509/26/00113357600116____33__1200x1200.jpg",
        "Origen: frances",
        54.99),
    LicorData("Ginebra Hendrick's",
        "Ginebra escocesa muy reconocida por su mezcla botanica con petalos de rosa bulgara y pepino. Su caracter floral y fresco la convierte en una opcion distinta dentro de las ginebras premium.",
        "https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/202302/28/00113357400194____12__1200x1200.jpg",
        "Origen: escoces",
        36.0),
    LicorData("Ginebra Tanqueray No. Ten",
        "Ginebra inglesa destilada con citricos enteros como pomelo, lima y naranja. Tiene un perfil mas brillante y refinado, pensado para gin tonics y combinados aromaticos.",
        "https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/202508/21/00113357400202____31__1200x1200.jpg",
        "Origen: ingles",
        38.99),
    LicorData("Ron Zacapa Solera Gran Reserva",
        "Ron de Guatemala envejecido en altura con el sistema solera. Combina dulzor, madera, frutos secos y especias en un perfil redondo que encaja muy bien como destilado de sobremesa.",
        "https://cdn.grupoelcorteingles.es/SGFM/dctm/MEDIA03/201911/19/00113358000159____17__640x640.jpg",
        "Origen: guatemalteco",
        39.95),
    LicorData("Vodka Beluga Noble",
        "Vodka de estilo artesanal elaborado con agua artesiana y alcohol de malta. Su acabado es limpio, equilibrado y suave, con una presentacion muy asociada al segmento premium.",
        "https://cdn.grupoelcorteingles.es/SGFM/dctm/MEDIA03/202310/06/00113357600405____18__1200x1200.jpg",
        "Origen: ruso",
        45.80)
)

fun getAllSearch() = listOf(
    SearchCard("https://s2.elespanol.com/2019/05/18/cocinillas/vinos/vinos_399470851_123155360_864x486.jpg",
        "Vinoteca El Sumiller",
        "Esta vinoteca de Aluche es la muestra de que no todo lo interesante ocurre en Madrid capital. A pesar de su reducido tamano (antes de vinoteca era una merceria de barrio), en la tienda de vinos de Juan, un informatico reconvertido en tendero y sumiller, no dejan de pasar cosas.",
        910419582,
        "Calle de Camarena, 88"),
    SearchCard("https://s2.elespanol.com/2019/05/18/cocinillas/vinos/vinos_399470886_123156344_864x486.jpg",
        "Espana en Cepa",
        "La gran sonrisa de Maria Sarmentera es la protagonista de esta vinoteca instalada en un viejo local de La Latina con suelos centenarios de baldosa hidraulica y muebles antiguos. Es ella quien atiende personalmente y copa de vino en mano a todo aquel que atraviesa las puertas del espacio buscando una experiencia sensorial.",
        910401278,
        "Plaza de Cascorro, 8. Madrid"),
    SearchCard("https://s2.elespanol.com/2019/05/18/cocinillas/vinos/vinos_399470896_123156637_864x486.jpg",
        "El Despacho Clandestino",
        "Este esquinazo secreto del barrio de Arganzuela no es una vinoteca al uso sino una coqueta tienda que, al estilo de los viejos almacenes de ultramarinos y los colmados de antano con un toque de speakeasy neoyorquino, nos invita a probar y adquirir un sinfin de apetecibles productos delicatessen.",
        918053101,
        "Calle del Plomo, esquina, Calle Onice 10. Madrid"),
    SearchCard("https://s2.elespanol.com/2019/05/18/cocinillas/vinos/vinos_399470974_123158831_854x640.jpg",
        "The One Wine",
        "Los responsables de este esquinazo de la plaza de los Chisperos de Chamberi son especialistas en encontrar joyas desconocidas por el gran publico. Su equipo de enologos, viticultores y sumilleres evalua el potencial de cada nuevo descubrimiento y da lugar a una carta de vinos nacionales e internacionales de interesante relacion calidad-precio.",
        910419582,
        "Calle de Manuel Cortina, 1. Madrid"),
    SearchCard("https://s2.elespanol.com/2019/05/18/cocinillas/vinos/vinos_399470925_123157449_864x486.jpg",
        "Vino & Compania",
        "La vinoteca mas querida por los vecinos de Chamberi se encuentra en plena plaza de Olavide y en sus estanterias muestra orgullosa sus mas de 800 referencias de vinos, cavas, champanes y licores de cualquier rincon de Espana y del mundo. Sin embargo, lo que mas destaca de esta tienda es la amabilidad y la atencion profesional y personalizada con la que atienden tanto a los aficionados como a los entendidos que se adentran en este rincon del vino.",
        914441278,
        "Plaza de Olavide, 5"),
    SearchCard("https://s2.elespanol.com/2019/05/18/cocinillas/vinos/vinos_399470781_123153413_855x1140.jpg",
        "Madrid & Darracott",
        "Madrid & Darracott abrio sus puertas en verano de 2018 a escasos metros de la Plaza Mayor, en el viejo corazon de Madrid, junto a Tirso de Molina, para hacer las delicias de fanaticos y curiosos. Esta vinoteca es el lugar ideal para descubrir vinos diferentes y compartir experiencias alrededor de los mismos",
        912191975,
        "Duque de Rivas, 8"
    )
)
