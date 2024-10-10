package test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import configuration.JpaConfig;
import eccezioni.SiglaTrenoException;
import entity.classi_astratte.TrenoBuilder;
import entity.classi_astratte.FabbricaVagoni;
import entity.classi_astratte.Vagone;
import entity.dao.ServizioDAO;
import entity.dao.TrenoDAO;
import entity.servizi.Servizio;
import entity.treno.Treno;
import fabbriche.FabbricaKargoModelz;
import fabbriche.FabbricaServizi;
import utility.Assemblatore;

public class Test03 {
    public static void main(String[] args) {
        
        FabbricaVagoni fabbricaKM= new FabbricaKargoModelz();
		TrenoBuilder builderKM = new Assemblatore(fabbricaKM);


		/*				TEST 03
		 * caricamento nel db di un treno con i rispettivi vagoni
		 * inserimento di un servizio al database
		 * 
         * cancellazione di un servizio
         * aggiunta di un servizio ad un vagone
		 * update di un treno
         * 
		 */


        String sigla = "hprp";
		try
		{
			AbstractApplicationContext context = new AnnotationConfigApplicationContext(JpaConfig.class);

			Treno trenoKM = builderKM.costruisciTreno("Treno Passeggeri KM",sigla);
			TrenoDAO trenoDAO = context.getBean(TrenoDAO.class);
            trenoDAO.salvaTreno(trenoKM);

            

            // NON SI AGGIUNGONO COSÌ! -> USARE UN SEEDER
            // AGGIUNTA DI UN SERVIZIO
            FabbricaServizi fabbricaServizi = new FabbricaServizi();
            ServizioDAO servizioDAO = context.getBean(ServizioDAO.class);
           

            // CANCELLAZIONE DI UN SERVIZIO  ->
            servizioDAO.eliminaServizioByName("bagno");

            // INSERIMENTO DI UN SERVIZIO
            servizioDAO.salvaServizio(fabbricaServizi.creaBagno());

            
            int indexVagone = 1;

            // AGGIUNTA DI UN SERVIZIO AD UN VAGONE
            Vagone vagoneKM = trenoKM.getVagone(indexVagone);
            //ERRORE: non va creato il servizio, va detto al DB di darcene uno!!
            Servizio s = servizioDAO.getServizioByName("bagno");
            //vagoneKM.addServizio(servizioDAO.getServizioByName("bagno"));
            vagoneKM.addServizio(s);
            
            System.out.println("Servizio: "+s.toString());
            
            servizioDAO.updateServizio(s);

            trenoKM.setVagone(indexVagone, vagoneKM);

            trenoDAO.updateTreno(trenoKM);


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
