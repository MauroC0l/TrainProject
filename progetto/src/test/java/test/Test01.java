package test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import configuration.JpaConfig;
import eccezioni.eccezioniSigla.SiglaTrenoException;
import entity.classi_astratte.FabbricaVagoni;
import entity.classi_astratte.TrenoBuilder;
import entity.classi_astratte.Vagone;
import entity.dao.UserDAO;
import entity.dao.VagoneDAO;
import entity.treno.Treno;
import entity.user.User;
import fabbriche.FabbricaKargoModelz;
import fabbriche.FabbricaRegionalGain;
import fabbriche.FabbricaXFurryFast;
import utility.Assemblatore;

public class Test01 {
    public static void main(String[] args) {
        
        FabbricaVagoni fabbricaKM= new FabbricaKargoModelz();
		TrenoBuilder builderKM = new Assemblatore(fabbricaKM);

		FabbricaVagoni fabbricaRG = new FabbricaRegionalGain();
		TrenoBuilder builderRG = new Assemblatore(fabbricaRG);

		FabbricaVagoni fabbricaFF = new FabbricaXFurryFast();
		TrenoBuilder builderFF = new Assemblatore(fabbricaFF);


		/*				TEST 01
		 * caricamento nel db di ogni tipologia di elemento del treno
		 * 
		 * 
		 * 
		 */


        String sigla = "hprpp";
		try
		{
			AbstractApplicationContext context = new AnnotationConfigApplicationContext(JpaConfig.class);
			
			VagoneDAO vagoneInterfaceDAO = context.getBean(VagoneDAO.class);

			User mazza = new User("Salvatore","Mazza", "salvatore.mazza@gmail.com", "Danzacudur0_04");
			UserDAO userDAO = context.getBean(UserDAO.class);
			userDAO.salvaUser(mazza);


			Treno trenoKM = builderKM.costruisciTreno("Treno Passeggeri KM",sigla,mazza);

			for (Vagone vagone : trenoKM.getListaVagoni()){
				vagoneInterfaceDAO.salvaVagone(vagone);
			}


			Treno trenoRG = builderRG.costruisciTreno("Treno Passeggeri RG",sigla,mazza);

			for (Vagone vagone : trenoRG.getListaVagoni()){
				vagoneInterfaceDAO.salvaVagone(vagone);
			}


			Treno trenoFF = builderFF.costruisciTreno("Treno Passeggeri FF",sigla,mazza);
			for (Vagone vagone : trenoFF.getListaVagoni()){

				vagoneInterfaceDAO.salvaVagone(vagone);
			}
            
			sigla = "hcc";

			trenoKM = builderKM.costruisciTreno("Treno Cargo KM",sigla,mazza);

			for (Vagone vagone : trenoKM.getListaVagoni()){
				vagoneInterfaceDAO.salvaVagone(vagone);
			}


			trenoRG = builderRG.costruisciTreno("Treno Cargo RG",sigla,mazza);

			for (Vagone vagone : trenoRG.getListaVagoni()){
				vagoneInterfaceDAO.salvaVagone(vagone);
			}


			trenoFF = builderFF.costruisciTreno("Treno Cargo F",sigla,mazza);
			for (Vagone vagone : trenoFF.getListaVagoni()){
				vagoneInterfaceDAO.salvaVagone(vagone);
			}


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
