package test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import configuration.JpaConfig;
import eccezioni.eccezioniSigla.SiglaTrenoException;
import entity.classi_astratte.FabbricaVagoni;
import entity.classi_astratte.TrenoBuilder;
import entity.classi_astratte.Vagone;
import entity.dao.ServizioDAO;
import entity.dao.TrenoDAO;
import entity.dao.UserDAO;
import entity.treno.Treno;
import entity.user.User;
import fabbriche.FabbricaKargoModelz;
import fabbriche.FabbricaRegionalGain;
import fabbriche.FabbricaServizi;
import utility.Assemblatore;

public class TestGenerale {
    public static void main(String[] args) {
        
        FabbricaVagoni fabbricaKM= new FabbricaKargoModelz();
		TrenoBuilder builderKM = new Assemblatore(fabbricaKM);
		
		FabbricaVagoni fabbricaRG= new FabbricaRegionalGain();
		TrenoBuilder builderRG = new Assemblatore(fabbricaRG);
		
		FabbricaVagoni fabbricaFF= new FabbricaKargoModelz();
		TrenoBuilder builderFF = new Assemblatore(fabbricaFF);
		
        String sigla = "hprp";
        try
		{
			AbstractApplicationContext context = new AnnotationConfigApplicationContext(JpaConfig.class);
		
            // 1.1 - CREAZIONE USER
            System.out.println("1.1 - CREAZIONE USER");
			User user1 = new User("Salvo","Mano", "salvo.mano@gmail.com", "Danzacudur0_04", 0.0);
			UserDAO userDAO = context.getBean(UserDAO.class);

            // 1.2 - SALVATAGGIO USER
            System.out.println("1.2 - SALVATAGGIO USER");
			userDAO.salvaUser(user1);

            	
            // 2.1 - CREAZIONE TRENI
            System.out.println("\n2.1 - CREAZIONE TRENI");
			Treno trenoKM = builderKM.costruisciTreno("Treno Cargo KM Passeggeri", sigla, user1, 2);
			Treno trenoRG = builderRG.costruisciTreno("Treno Cargo RG Passeggeri", sigla, user1, 3);
			Treno trenoFF = builderFF.costruisciTreno("Treno Cargo FF Passeggeri", sigla, user1, 1);


            // 1.3 - UPDATE DI UN UTENTE : CHECKED
            System.out.println("1.3 - UPDATE DI UN UTENTE");
            user1.setNome("Salvino");
            userDAO.updateUser(user1);


            //2.2 - SALVATAGGIO TRENI
            System.out.println("2.2 - SALVATAGGIO TRENI");
			TrenoDAO trenoDAO = context.getBean(TrenoDAO.class);
            trenoDAO.salvaTreno(trenoKM);
            trenoDAO.salvaTreno(trenoRG);
            trenoDAO.salvaTreno(trenoFF);

            // 2.3 - UPDATE DI UN TRENO : CHECKED
            System.out.println("2.3 - UPDATE TRENO: KM");
            trenoKM.setNomeTreno("Treno molto bello");
            trenoDAO.updateTreno(trenoKM); //controlla sul db il nome

            // 2.4 - RIMOZIONE DI UN TRENO : CHECKED
            System.out.println(("2.4 - RIMOZIONE TRENO: FF"));
            trenoDAO.eliminaTrenoById(trenoFF.getId());


            // 3.1 - RIMOZIONE DI UN VAGONE : CHECKED
            System.out.println(("3.1 - RIMOZIONE VAGONE"));

            trenoDAO.eliminaVagoneByIndex(trenoRG, 2); //con 1-2- va , con 0-3 non deve andare
            System.out.println(trenoRG);


            // TODO: 3.2 MODIFICA DI UN VAGONE

           
                         
           /*  // 4.0 - RIMOZIONE SERVIZIO 
            System.out.println("4.0 - RIMOZIONE SERVIZIO");
            ServizioDAO servizioDAO0= context.getBean(ServizioDAO.class);
            servizioDAO0.eliminaServizioByName("bagno"); */

            // 4.1 - CREAZIONE SERVIZIO : CHECKED
            System.out.println("4.1 - CREAZIONE SERVIZIO");
            FabbricaServizi fabbricaServizi = new FabbricaServizi();
            ServizioDAO servizioDAO = context.getBean(ServizioDAO.class);

            // 4.2 - SALVATAGGIO DI UN SERVIZIO :  CHECKED 
            // potenziale ERRORE!!! se c'è già un servizio con quel nome
            System.out.println("4.2 - SALVATAGGIO SERVIZIO");
            servizioDAO.salvaServizio(fabbricaServizi.creaBagno());


            // 4.3 - ASSEGNAZIONE SERVIZIO (ad un vagone) : 
           // System.out.println("4.3 - ASSEGNAZIONE SERVIZIO");
           // trenoDAO.addServizio(trenoKM, 1, "bagno");
                        //dsadsa

            // ACQUISTO DI UN TRENO
            // TODO: controllo da fare

            // VOTAZIONE TRENO
            // TODO: controllo da fare

            
            // RIMOZIONE DI UN UTENTE : CHECKED
             User user2 = new User("User","Da rimuovere", "user.darimuovere@gmail.com", "Danzacudur0_04", 0.0);
             userDAO.salvaUser(user2);
             userDAO.eliminaUserById(user2.getId_User());

             

            // --- QUERY USER ---

            // user by name
            System.out.println("\n\n1. USER BY NAME");
            System.out.println(userDAO.getUserByName("Salvino"));

            // --- QUERY USER --- 


            // --- QUERY TRENO --- 

            // treno by ID
            System.out.println("\n\n1. TRENO BY ID");
            System.out.println(trenoDAO.getTrenoByMarca(1));

            // treno by name
            System.out.println("\n\n2. TRENO BY NAME");
            System.out.println(trenoDAO.getTrenoByName("Treno molto bello"));

            // treno by pesoTrasportabile
            System.out.println("\n\n3. TRENO BY PESO TRASPORTABILE");
            System.out.println(trenoDAO.getTreniByPesoTrasportabile(500));

            // --- QUERY TRENO --- 



       
			context.close();
			
		}
		catch (SiglaTrenoException e)
		{
			String message = e.getMessage();
			System.out.println(message);
			System.out.println(e.getSuggerimento());
		}





    }
}
