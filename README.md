**Scaner de Produse - Verificare E-uri**

Un program în Java, cu interfață grafică (Swing), care verifică dacă un produs conține E-uri periculoase. 
Utilizatorul introduce numele produsului și lista de E-uri, iar aplicația oferă o evaluare a acestuia și permite salvarea produsului împreună cu evaluarea într-un fișier. 
Totodată, aplicația oferă o funcționalitate de afișare a unei diagrame circulare privind distribuția produselor pe categorii de periculozitate.

<img width="390" height="257" alt="image" src="https://github.com/user-attachments/assets/5708b8d4-bd8a-4f34-9b58-db935ced66e2" />


**Funcționalitatea**

Introduci numele unui produs și codurile E găsite pe ambalaj (ex: `E-101`), iar programul îți spune dacă produsul e sigur, riscant sau periculos. Pentru fiecare produs adăugat, apare și o evaluare clară:

-  **Produsul este sigur**
-  **Produsul este riscant**
-  **Produsul este extrem de periculos! Evită-l complet!**

<img width="390" height="280" alt="image" src="https://github.com/user-attachments/assets/90740c3d-cebb-483d-8c45-70b9896f6a86" />


Toate produsele verificate se salvează într-un fișier (`produse.txt`), ca să le poți vedea și mai târziu.

Programul are și un grafic (folosind JFreeChart) care arată statistici despre produsele verificate.

<img width="390" height="257" alt="image" src="https://github.com/user-attachments/assets/203dfd16-0928-41ae-aa59-273b700386c4" />



**Instrumente folosite**

- **Java** limbajul de programare
- **Swing** pentru interfața grafică 
- **JFreeChart** pentru graficul cu statistici
- **Fișiere text** (`date.txt`, `produse.txt`) pentru salvarea datelor, fără bază de date
