Projet P6.

Ce projet requiert Java 11 pour fonctionner. Pour préparer le dossier de distribution (dist), lancer le script
dist/scripts/dist(.sh ou .bat). Ce script va préparer Gradle, installer les dépendances, compiler les sources et créer
les archives Jar de l'interface en ligne de commandes (CLI) dans dist/cli/lib puis créer la Javadoc dans dist/docs. La
première exécution peut prendre plusieurs minutes. Le CLI peut être utilisé avec le script dist/scripts/cli depuis
n'importe quel dossier. Le dossier de travail sera ajouté au chemin de classes Java, afin de permettre le chargement de
bibliothèques P6 avec le système de services Java. Des programmes P6 sont disponibles dans le dossier dist/demos. Vous
pouvez par exemple lancer le programme du crible d'Eratosthène avec la commande
./dist/scripts/cli.sh dist/demos/crible.txt 2000 500
où les nombres 2000 et 500 sont respectivement la cible d'itérations et la cible de stabilité (lire les commentaires
dans les fichiers de programme). Pour plus d'informations sur le CLI, exécuter le script avec le drapeau --help.
Afin d'utiliser la bibliothèque MyLibrary définie dans dist/demos/demo, compiler les fichiers de démonstration Java avec
le script compileDemo, puis exécuter CLI normalement en se trouvant dans le dossier dist/demos (là où le
dossier META-INF nécessaire aux services Java se trouve).
Sont aussi présentes des démonstrations utilisant l'API du noyau et du registre (compilées par compileDemo).
Elles peuvent être exécutées avec le script runDemo en passant comme argument le nom de la classe correspondant à la
démonstration.
Pour finir, le dossier de distribution peut être nettoyé avec le script clean.
