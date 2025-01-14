## 1. Singleton Pattern
* Pattern-ul este aplicat clasei ```Game```, care contine o instanta a jocului in interiorul sau, initial nula.
* Constructorul este privat si este apelat de metoda getInstance(), care creeaza un obiect nou prin ```lazy instantiation``` sau returneaza obiectul existent.

## 2. Builder pattern
* Pattern-ul este aplicat clasei ```Information```, prezenta in mai multe clase (Account, Spell, Entity).
* Contine toate datele despre clasele de mai sus, iar parametri nefolositi sunt nuli.

## 3. Factory Pattern
* Pattern-ul este aplicat clasei ```CharacterFactory```, care creeaza mai multe tipuri de caractere (Mage, Rogue, Warrior).
* Clasa nu poate fi instantiata, avand un constructor privat, iar metoda pentru creearea caracterelor este statica.

## 4. Visitor pattern
* Pattern-ul este aplicat claselor ```Spell``` (visitor), ```Character``` si ```Entity```.
* Spell este o clasa abstracta si, deci, Visitor-ul este implementat de cele 3 abilitati (Fire, Ice, Earth).
* Visitor-ul viziteaza ```Mage```, ```Rogue```, ```Warrior``` si ```Enemy``` si verifica daca sunt imuni la abilitate, iar daca nu sunt aplica atacul.

## 5. Interfata grafica
* Jocul poate fi inceput in 2 moduri, din clasa Game pentru interfata text (in terminal)
sau din clasa ```Test()``` pentru interfata grafica. Initial se creeaza frame-ul de inceput
in care utilizatorul este intrebat in ce mod vrea sa se joace (```CLI``` sau ```GUI```)
Se foloseste un GridBagLayout pentru asezarea intrebarii si a celor 2 butoane. Fiecare
buton are un action listener care va inchide frame-ul curent si va incepe interfata
corespunzatoare. Aceste butoane sunt stilizate in metoda ```createStylishButton()```.
* Daca modul de joc ales este GUI, se va crea o alta fereastra unde utilizatorul
se va loga. Daca contul sau parola introduse nu sunt corecte se va incheia executia
programului. Tot folosindu-se un ```GridBagLayout``` impreuna cu ```GridBagConstraints``` se adauga
label-urile de ```email``` si ```password``` si field-urile corespunzatoare impreuna cu
butonul de login care are un action listener care verifica logica de logare (contul si parola).
* Daca contul si parola sunt valide, se incepe jocul in interfata grafica, in clasa ```GameGUI```.
Se apeleaza metoda de ```setupUI()```, in care se afiseaza caracterele curente ale jucatorului
sub forma de panel-uri. Fiecare caracter are o imagine care este scalata la 300x300 pixeli.
Imaginea este pozitionata deasupra in timp ce statisticile sunt pozitionate dedeusbt, sub forma
de textfield-uri needitabile, cu bordura transparenta si cursor de culoarea background-ului pentru a fi
invizibil. Fiecare panel al unui caracter are un buton ```Select``` cu un ```ActionListener``` care
va selecta caracterul si va apela ```setupGameUI()```, dupa ce curata frame-ul curent.
* In ```setupGameUI()``` se creeaza harta (grid-ul) cu butoanele de control (North, South etc.) si
statisticile jucatorului la momentul curent. Se foloseste o matrice de butoane pentru harta, fiecare
buton fiind gol, ca apoi sa aiba un icon corespunzator (daca a fost vizitat sau daca e celula curenta,
unde se afla jucatorul). Fiecare buton de control are un action listener care va muta caracterul pe
harta ca apoi celula noua sa fie procesata in metoda ```processCell()```. In ```updateStatsLabel()```,
se reimprospateaza statisticile caracterului la momentul curent, metoda apelata dupa o celula de tip
sanctuar sau dupa o lupta.
* In ```processCell()```, sunt 3 moduri de a interpreta celula, ```ENEMY```, ```SANCTUARY```, ```PORTAL```.
Daca celula e de tip SANCTUARY, se actualizeaza viata si mana; de tip PORTAL, experienta caracterului
este crescuta si daca trece de pragul de experienta, nivelul este incrementat si trasaturile caracterului
incrementate. Nivelul si numarul de jocuri sunt incrementate. Programul se reseteaza. In schimb,
daca celula este de tip ENEMY, se creeaza un inamic nou si frame-ul este construit de asa natura
incat sa fie compus din panel-urile caracterului si al inamicului si cel al butoanelor in care se poate
selecta un atac normal sau o abilitate. Daca se selecteaza o abilitate, se afiseaza un grid scroll-abil 
cu icon-urile si statisticile corespunzatoare imaginilor. Daca se selecteaza o abilitate si caracterul
are mana suficienta, se va executa, altfel se va afisa un avertisment si se va sugera un atac normal.
* Daca jucatorul a castigat, se va continua jocul, executia programului reintorcandu-se la grid, altfel
se va afisa pagina finala cu statisticile jucatorului in ```finalPage()```. Aceasta contine imaginea
caracterului curent cu statisticile sale si 2 butoane, ```Continue``` si ```Exit```. Apasarea butonului
de Continue va restarta jocul, in timp ce Exit va inchide executia programului.

### Grad de dificultate: mediu-greu
### Timpul alocat rezolvarii: ~ 4 zile pentru GUI
