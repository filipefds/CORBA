
import CalcApp.*;
import CalcApp.CalcPackage.DivisionByZero;

import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

import java.util.Properties;

class CalcImpl extends CalcPOA {

    @Override
    public float sum(float a, float b) {
        return a + b;
    }

    @Override
    public float div(float a, float b) throws DivisionByZero {
        if (b == 0) {
            throw new CalcApp.CalcPackage.DivisionByZero();
        } else {
            return a / b;
        }
    }

    @Override
    public float mul(float a, float b) {
        return a * b;
    }

    @Override
    public float sub(float a, float b) {
        return a - b;
    }
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }
}

public class CalcServer {

    public static void main(String args[]) {
        try {
            // Inicialização do ORB
            ORB orb = ORB.init(args, null);
	    // obtém referência ao rootpoa e ativa o POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
	    // cria o servidor e registra no ORB
            CalcImpl helloImpl = new CalcImpl();
            helloImpl.setORB(orb);
	    // obtém referência de objeto do servidor
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
            Calc href = CalcHelper.narrow(ref);
	    // obtém o contexto de nomenclatura da raiz
            // NameService invoca o serviço de nomes
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	    // Use NamingContextExt que faz parte do Interoperable
            // Especificação do serviço de nomenclatura (INS).
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	    // vincula a referência de objeto na nomenclatura
            String name = "Calc";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);

            System.out.println("Servidor CORBA pronto e aguardando...");

            // espera por requisição de clientes
	    orb.run();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }

        System.out.println("Exiting ...");

    }
}
