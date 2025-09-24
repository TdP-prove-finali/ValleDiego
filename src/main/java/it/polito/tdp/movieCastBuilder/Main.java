package it.polito.tdp.movieCastBuilder;

public class Main {
	public static void main(String[] args) {
        EntryPoint.main(args);
    }
}
/* MODIFICHE RISPETTO ALLA PROPOSTA DI TESI
 * 
 *  - AGGIUNTA NUOVA TABELLA
 *  
 *  Ho riscontrato il problema che spesso l'output dava nel cast attori già morti, per risolvere il problema ho aggiunto al dataset una nuova tabella,
 *  contenente informazioni su persone che lavorano nel mondo del cinema (nome, data di nascita, data di morte, professione, e "KnowForTitle", la sua opera più famosa)
 *  Questa tabella, contenente 530000 righe, contiene quasi tutti i 2700 attori presenti nel mio dataset, ma ha generato un nuove problematiche da risolvere: 
 *  molti nomi erano ripetuti (omonimi), e non ho modo di sapere quale degli omonimi è l'attore che cerco.
 *  
 *  Inanzitutto, per evitare omonimi tra vivi e morti, considero l'attore solo se la data di nascita non è più piccola di al più 6 anni dalla data di debutto
 *  del film (ipotizzando che alcuni di questi attori possono aver recitato da bambini)
 *  Dopodichè procedo a tentativi. Controllo in questo ordine se:
 *  1- KnowForTitle è presente nella lista dei film dell'attore
 *  2- Utilizzando la classe JaroWinkler presente nella libreria java-string-similarity, scelgo l'attore il cui "KnowForTitle" ha somiglianza con uno dei film
 *  presi dal dataset (il valore per cui li considero simili, dopo alcune prove, è di 0.62)(passaggio utile soprattuto per film come harry potter o il signore degli 
 *  anelli)
 *  3- in assenza di somiglianze, scelgo chi dei 2 contiene "actor" o "actress" nella professione (Potrebbe sembrare il metodo migliore fin da subito, ma genera errori,
 *  perchè non sempre l'attore giusto contiene "actor" o "actress" nella professione, per esempio capita spesso in "Animation"
 *  4- Se nessuno di questi metodi riscuote successo, non ho modo di sapere chi è quello giusto, e scelgo a caso. 
 *  
 *   Queste regole mi hanno aiutato a risolvere tutti i problemi con gli omonimi che avevo individuato e, anche se teoricamente è possibile, non ho trovato alcun attore
 *   sbagliato nei risultati. C'è da sottolineare che dovessi prendere l'attore sbagliato non avrebbe alcuna importanza nell'algoritmo, significherebbe solo che nell'ouput 
 *   finale età dell'attore e professione sono sbagliate. L'unico modo per ottenere un risultato migliore di questo sarebbe avere un dataset che colleghi ad ogni attore ogni
 *   film che abbia mai fatto, non solo uno.
 * 
 *  - GESTIONE DATI MANCANTI
 *  
 *  Alcuni dati nei film meno famosi, sonon purtroppo mancanti (un centinaio in GROSS, e in metascore)
 *  Piuttosto che non considerare questi film, ho scelto di attribuire a quei dati il minore valore raggiunto in quelle caratterisitche mancanti 
 *  Ho provato a chiedere a chatgpt di darmi tabelle con i dati macnanti, e anche se non tutti, ha generato un output che ad un controllo a campione sembra corretto,
 *  però per il momento ho preferito non aggiungerli al dataset originale 
 * 
 * - CONSIDERO NELLA RICORSIONE SOLO I PRIMI 180 ATTORI PIù PRESTANTI
 * 
 * Scelta effetuata per ridurre i tempi della ricorsione a 2 secondi. Così facendo, il codice ti da un risultato entro 5 secondi
 * Ho cercato in tutti i modi di ridurre il tempo, al momento ho 2 proposte di ricorsione che danno gli stessi risultati (2 secondi)
 * i 180 attori scelti sono quelli che hanno statActor più alti, e dopo varie prove ho visto che l'output finale è sempre lo stesso che avrebbe avuto una ricorsione con 
 * molti più attori (questo è possibile perchè alla fine la ricorsione trova il valore della sintonia del cast, che rappresenta solo 1/6 del risultato finale)
 * 
 * 
 * 
 * */
 