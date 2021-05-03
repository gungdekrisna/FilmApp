package com.example.film.utils

import com.example.film.R
import com.example.film.data.FilmEntity
import com.example.film.data.TvShowEntity

object DataGenerator {
    fun generateFilms() : List<FilmEntity>{
        val films = ArrayList<FilmEntity>()

        films.add(
            FilmEntity(
                "f1",
                "A Star Is Born",
                "10/05/2018",
                "Seasoned musician Jackson Maine discovers — and falls in love with — struggling artist Ally. She has just about given up on her dream to make it big as a singer — until Jack coaxes her into the spotlight. But even as Ally's career takes off, the personal side of their relationship is breaking down, as Jack fights an ongoing battle with his own internal demons.",
                "Drama, Romance, Music",
                R.drawable.poster_a_star_is_born,
                "Bradley Cooper",
                "75",
                "2h 16m"
            )
        )

        films.add(
            FilmEntity(
                "f2",
                "Alita: Battle Angel",
                "02/14/2019",
                "When Alita awakens with no memory of who she is in a future world she does not recognize, she is taken in by Ido, a compassionate doctor who realizes that somewhere in this abandoned cyborg shell is the heart and soul of a young woman with an extraordinary past.",
                "Action, Science Fiction, Adventure",
                R.drawable.poster_alita,
                "Robert Rodriguez",
                "72",
                "2h 2m"
            )
        )

        films.add(
            FilmEntity(
                "f3",
                "Aquaman",
                "12/21/2018",
                "Once home to the most advanced civilization on Earth, Atlantis is now an underwater kingdom ruled by the power-hungry King Orm. With a vast army at his disposal, Orm plans to conquer the remaining oceanic people and then the surface world. Standing in his way is Arthur Curry, Orm's half-human, half-Atlantean brother and true heir to the throne.",
                "Action, Adventure, Fantasy",
                R.drawable.poster_aquaman,
                "James Wan",
                "69",
                "2h 23m"
            )
        )

        films.add(
            FilmEntity(
                "f4",
                "Bohemian Rhapsody",
                "11/02/2018",
                "Singer Freddie Mercury, guitarist Brian May, drummer Roger Taylor and bass guitarist John Deacon take the music world by storm when they form the rock 'n' roll band Queen in 1970. Hit songs become instant classics. When Mercury's increasingly wild lifestyle starts to spiral out of control, Queen soon faces its greatest challenge yet – finding a way to keep the band together amid the success and excess.",
                "Music, Drama, History",
                R.drawable.poster_bohemian,
                "Anthony McCarten",
                "80",
                "2h 15m"
            )
        )

        films.add(
            FilmEntity(
                "f5",
                "Cold Pursuit",
                "02/08/2019",
                "The quiet family life of Nels Coxman, a snowplow driver, is upended after his son's murder. Nels begins a vengeful hunt for Viking, the drug lord he holds responsible for the killing, eliminating Viking's associates one by one. As Nels draws closer to Viking, his actions bring even more unexpected and violent consequences, as he proves that revenge is all in the execution.",
                "Action, Crime, Thriller",
                R.drawable.poster_cold_persuit,
                "Hans Petter Moland",
                "57",
                "1h 59m"
            )
        )

        films.add(
            FilmEntity(
                "f6",
                "Creed II",
                "11/21/2018",
                "Between personal obligations and training for his next big fight against an opponent with ties to his family's past, Adonis Creed is up against the challenge of his life.",
                "Drama",
                R.drawable.poster_creed,
                "Steven Caple Jr.",
                "69",
                "2h 10m"
            )
        )

        films.add(
            FilmEntity(
                "f7",
                "Fantastic Beasts: The Crimes of Grindelwald",
                "11/16/2018",
                "Gellert Grindelwald has escaped imprisonment and has begun gathering followers to his cause—elevating wizards above all non-magical beings. The only one capable of putting a stop to him is the wizard he once called his closest friend, Albus Dumbledore. However, Dumbledore will need to seek help from the wizard who had thwarted Grindelwald once before, his former student Newt Scamander, who agrees to help, unaware of the dangers that lie ahead. Lines are drawn as love and loyalty are tested, even among the truest friends and family, in an increasingly divided wizarding world.",
                "Adventure, Fantasy, Drama",
                R.drawable.poster_crimes,
                "David Yates",
                "69",
                "2h 14m"
            )
        )

        films.add(
            FilmEntity(
                "f8",
                "Glass",
                "01/18/2019",
                "In a series of escalating encounters, former security guard David Dunn uses his supernatural abilities to track Kevin Wendell Crumb, a disturbed man who has twenty-four personalities. Meanwhile, the shadowy presence of Elijah Price emerges as an orchestrator who holds secrets critical to both men.",
                "Thriller, Drama, Science Fiction",
                R.drawable.poster_glass,
                "M. Night Shyamalan",
                "67",
                "2h 9m"
            )
        )

        films.add(
            FilmEntity(
                "f9",
                "How to Train Your Dragon: The Hidden World",
                "02/22/2019",
                "As Hiccup fulfills his dream of creating a peaceful dragon utopia, Toothless’ discovery of an untamed, elusive mate draws the Night Fury away. When danger mounts at home and Hiccup’s reign as village chief is tested, both dragon and rider must make impossible decisions to save their kind.",
                "Animation, Family, Adventure",
                R.drawable.poster_how_to_train,
                "Dean DeBlois",
                "78",
                "1h 44m"
            )
        )

        films.add(
            FilmEntity(
                "f10",
                "Avengers: Infinity War",
                "04/27/2018",
                "As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate of Earth and existence itself has never been more uncertain.",
                "Adventure, Action, Science Fiction",
                R.drawable.poster_infinity_war,
                "Anthony Russo",
                "83",
                "2h 29m"
            )
        )

        return films
    }

    fun generateTvShows() : List<TvShowEntity>{
        val tvShows = ArrayList<TvShowEntity>()

        tvShows.add(
            TvShowEntity(
                "t1",
                "Arrow",
                "2012",
                "Spoiled billionaire playboy Oliver Queen is missing and presumed dead when his yacht is lost at sea. He returns five years later a changed man, determined to clean up the city as a hooded vigilante armed with a bow.",
                "Crime, Drama, Mystery, Action & Adventure",
                R.drawable.poster_arrow,
                "Greg Berlanti",
                "66",
                "42m"
            )
        )

        tvShows.add(
            TvShowEntity(
                "t2",
                "Doom Patrol",
                "2019",
                "The Doom Patrol’s members each suffered horrible accidents that gave them superhuman abilities — but also left them scarred and disfigured. Traumatized and downtrodden, the team found purpose through The Chief, who brought them together to investigate the weirdest phenomena in existence — and to protect Earth from what they find.",
                "Sci-Fi & Fantasy, Comedy, Drama",
                R.drawable.poster_doom_patrol,
                "Jeremy Carver",
                "76",
                "49m"
            )
        )

        tvShows.add(
            TvShowEntity(
                "t3",
                "Dragon Ball",
                "1986",
                "Long ago in the mountains, a fighting master known as Gohan discovered a strange boy whom he named Goku. Gohan raised him and trained Goku in martial arts until he died. The young and very strong boy was on his own, but easily managed. Then one day, Goku met a teenage girl named Bulma, whose search for the mystical Dragon Balls brought her to Goku's home. Together, they set off to find all seven and to grant her wish.",
                "Animation, Action & Adventure, Sci-Fi & Fantasy",
                R.drawable.poster_dragon_ball,
                "Akira Toriyama",
                "81",
                "25m"
            )
        )

        tvShows.add(
            TvShowEntity(
                "t4",
                "Family Guy",
                "1999",
                "Sick, twisted, politically incorrect and Freakin' Sweet animated series featuring the adventures of the dysfunctional Griffin family. Bumbling Peter and long-suffering Lois have three kids. Stewie (a brilliant but sadistic baby bent on killing his mother and taking over the world), Meg (the oldest, and is the most unpopular girl in town) and Chris (the middle kid, he's not very bright but has a passion for movies). The final member of the family is Brian - a talking dog and much more than a pet, he keeps Stewie in check whilst sipping Martinis and sorting through his own life issues.",
                "Animation, Comedy",
                R.drawable.poster_family_guy,
                "Seth MacFarlane",
                "70",
                "22m"
            )
        )

        tvShows.add(
            TvShowEntity(
                "t5",
                "The Flash",
                "2014",
                "After a particle accelerator causes a freak storm, CSI Investigator Barry Allen is struck by lightning and falls into a coma. Months later he awakens with the power of super speed, granting him the ability to move through Central City like an unseen guardian angel. Though initially excited by his newfound powers, Barry is shocked to discover he is not the only \"meta-human\" who was created in the wake of the accelerator explosion -- and not everyone is using their new powers for good. Barry partners with S.T.A.R. Labs and dedicates his life to protect the innocent. For now, only a few close friends and associates know that Barry is literally the fastest man alive, but it won't be long before the world learns what Barry Allen has become...The Flash.",
                "Drama, Sci-Fi & Fantasy",
                R.drawable.poster_flash,
                "Greg Berlanti, Geoff Johns, Andrew Kreisberg",
                "77",
                "44m"
            )
        )

        tvShows.add(
            TvShowEntity(
                "t6",
                "Gotham",
                "2014",
                "Everyone knows the name Commissioner Gordon. He is one of the crime world's greatest foes, a man whose reputation is synonymous with law and order. But what is known of Gordon's story and his rise from rookie detective to Police Commissioner? What did it take to navigate the multiple layers of corruption that secretly ruled Gotham City, the spawning ground of the world's most iconic villains? And what circumstances created them – the larger-than-life personas who would become Catwoman, The Penguin, The Riddler, Two-Face and The Joker?",
                "Drama, Crime, Sci-Fi & Fantasy",
                R.drawable.poster_gotham,
                "Bruno Heller",
                "75",
                "43m"
            )
        )

        tvShows.add(
            TvShowEntity(
                "t7",
                "Grey's Anatomy",
                "2005",
                "Follows the personal and professional lives of a group of doctors at Seattle’s Grey Sloan Memorial Hospital.",
                "Drama",
                R.drawable.poster_grey_anatomy,
                "Shonda Rhimes",
                "82",
                "43m"
            )
        )

        tvShows.add(
            TvShowEntity(
                "t8",
                "Hanna",
                "2019",
                "This thriller and coming-of-age drama follows the journey of an extraordinary young girl as she evades the relentless pursuit of an off-book CIA agent and tries to unearth the truth behind who she is. Based on the 2011 Joe Wright film.",
                "Action & Adventure, Drama",
                R.drawable.poster_hanna,
                "David Farr",
                "75",
                "50m"
            )
        )

        tvShows.add(
            TvShowEntity(
                "t9",
                "Marvel's Iron Fist",
                "2017",
                "Danny Rand resurfaces 15 years after being presumed dead. Now, with the power of the Iron Fist, he seeks to reclaim his past and fulfill his destiny.",
                "Action & Adventure, Drama, Sci-Fi & Fantasy",
                R.drawable.poster_iron_fist,
                "Scott Buck",
                "66",
                "55m"
            )
        )

        tvShows.add(
            TvShowEntity(
                "t10",
                "Naruto Shippūden",
                "2007",
                "Naruto Shippuuden is the continuation of the original animated TV series Naruto.The story revolves around an older and slightly more matured Uzumaki Naruto and his quest to save his friend Uchiha Sasuke from the grips of the snake-like Shinobi, Orochimaru. After 2 and a half years Naruto finally returns to his village of Konoha, and sets about putting his ambitions to work, though it will not be easy, as He has amassed a few (more dangerous) enemies, in the likes of the shinobi organization; Akatsuki.",
                "Animation, Action & Adventure, Sci-Fi & Fantasy",
                R.drawable.poster_naruto_shipudden,
                "Masashi Kishimoto",
                "86",
                "25m"
            )
        )

        return tvShows
    }
}