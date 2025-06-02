# Ticketing System – Rendszerterv

## 1. A rendszer célja

A rendszer célja egy jegykezelő (ticketing) platform biztosítása különböző hibák, problémák vagy felhasználói igények kezelésére. A rendszer lehetővé teszi a hibajegyek létrehozását, kezelését, priorizálását, szűrését, továbbá jogosultságkezelést is biztosít két szerepkör számára: adminisztrátor és felhasználó.

## 2. Projektterv

### 2.1 Fejlesztői környezet

- Fejlesztőeszköz: IntelliJ IDEA (Java)
- Verziókezelés: Git
- Adatbázis: MySQL (helyi gépen)
- Tesztelési keretrendszer: JUnit 5
- GUI: Java Swing
- Tervezési minták: Factory, Observer, Strategy, Repository

### 2.2 Résztvevők

| Név            | Szerep         |
|----------------|----------------|
| Fejlesztő      | Full-stack Java fejlesztő |
| Tesztelő       | Unit és integrációs tesztek |
| Adminisztrátor | Adatbázis és rendszerkarbantartás |

### 2.3 Ütemterv

| Fázis              | Időtartam               |
|--------------------|-------------------------|
| Rendszerterv       | 2025.05.01 – 2025.05.03 |
| Fejlesztés         | 2025.05.03 – 2025.05.24 |
| Tesztelés          | 2025.05.24 – 2025.05.31 |

## 3. Üzleti folyamatok modellje

A rendszer támogatja a hibajegyek teljes életútját:
- jegy létrehozása
- prioritás alapú határidő kiszámítása
- hozzárendelés admin által
- értesítés küldése
- jegy lezárása

A felhasználók csak saját jegyeiket látják, míg az admin minden jegyhez hozzáfér és módosíthatja azokat.

## 4. Követelmények

### 4.1 Funkcionális követelmények

- Bejelentkezés (jogosultságalapú)
- Jegy létrehozása, módosítása
- Jegyek szűrése, listázása
- Értesítések kezelése
- Felhasználók hozzárendelése jegyekhez (admin)
- Jogosultságkezelés

### 4.2 Nem funkcionális követelmények

- Stabil működés
- Naplózás fájlba
- Minimum 5 párhuzamos felhasználó kiszolgálása
- Átlátható kód, moduláris szerkezet

## 5. Funkcionális terv

### 5.1 Rendszer-szereplők

| Szerepkör    | Jogosultságok |
|--------------|----------------|
| Admin        | Teljes hozzáférés, jegyek és felhasználók kezelése |
| Felhasználó  | Saját jegyek létrehozása, listázása |

### 5.2 Menü-hierarchia

```
Login
 └── Főmenü
     ├── Új hibajegy létrehozása
     ├── Hibajegyek listázása (szűrési lehetőségekkel)
     ├── Értesítések megtekintése
     └── Kijelentkezés
```

## 6. Adatmodell és adatbázis terv

### 6.1 Főbb táblák

| Tábla          | Leírás                        |
|----------------|-------------------------------|
| users          | Felhasználói fiókok           |
| tickets        | Hibajegyek                    |
| notifications  | Értesítések                   |

## 7. Felhasználói felület

- **Bejelentkezés**: felhasználónév, jelszó
- **Jegy létrehozás**: cím, leírás, prioritás, típus
- **Jegy lista**: szűrő (státusz, prioritás, kulcsszó)
- **Jegy részletek**: csak admin szerkeszthet
- **Értesítések**: időbélyegzett lista

## 8. Architektúrális terv

- **Kliens**: Java Swing GUI
- **Backend**: Java alapú osztálylogika
- **Adatbázis**: MySQL helyben
- **Logolás**: fájlba (időbélyeggel)

## 9. Naplózás és hibakezelés

- Minden bejelentkezés, jegy művelet és hiba logolásra kerül
- Példa:
  ```
  [2025.06.01 14:23:00] INFO: User 'admin' logged in
  [2025.06.01 14:25:34] ERROR: Failed to save ticket
  ```
  
## 10. Implementációs terv

- Verziókezelés: Git
- Tesztelés: JUnit 5, Mockito (mock példányokkal)
- Mintaalapú implementáció:
    - Abstract Factory (jegy típusok)
    - Observer (értesítések)
    - Strategy (prioritás és keresés)
    - Repository (adatkezelés elrejtése)

## 11. Tesztelés

- **Unit teszt**: Strategy, Factory, Observer logikákra
- **Integrációs teszt**: GUI – Controller – Repo útvonalak
- **Felhasználói teszt**: Jegy létrehozása, módosítása, listázása
- **Hiba szimuláció**: adatbázis elérhetetlenség esetén

## 12. Biztonság

- Jelszavas autentikáció
- Jogosultság-alapú GUI vezérlés
- Naplózott műveletek
- SQL injection elleni védelem (PreparedStatement használat)


