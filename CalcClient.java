
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import CalcApp.*;
import CalcApp.CalcPackage.DivisionByZero;

import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import static java.lang.System.out;

public class CalcClient {

    static Calc calcImpl;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String args[]) {

        try {
            // Inicializando o ORB
            ORB orb = ORB.init(args, null);
	    // obtém o contexto de nomenclatura da raiz
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	    // Use NamingContextExt em vez de NamingContext. Isto é parte do Serviço de nomenclatura interoperável.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	    // resolve a referência de objeto na nomeação
            String name = "Calc";
            calcImpl = CalcHelper.narrow(ncRef.resolve_str(name));

            while (true) {
                out.println("1. Soma");
                out.println("2. Subtração");
                out.println("3. Multiplicação");
                out.println("4. Divisão");
                out.println("5. exit");
                out.println("-----------------------------");
                out.println("Escolha a operação desejada: ");

                try {
                    String opt = br.readLine();
                    if (opt.equals("5")) {
                        break;
                    } else if (opt.equals("1")) {
                        out.println("a+b= " + calcImpl.sum(getFloat("a"), getFloat("b")));
                    } else if (opt.equals("2")) {
                        out.println("a-b= " + calcImpl.sub(getFloat("a"), getFloat("b")));
                    } else if (opt.equals("3")) {
                        out.println("a*b= " + calcImpl.mul(getFloat("a"), getFloat("b")));
                    } else if (opt.equals("4")) {
                        try {
                            out.println("a/b= " + calcImpl.div(getFloat("a"), getFloat("b")));
                        } catch (DivisionByZero de) {
                            out.println("Division by zero!!!");
                        }
                    }
                } catch (Exception e) {
                    out.println("===");
                    out.println("Error with numbers");
                    out.println("===");
                }
                out.println("");

            }

        } catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace(System.out);
        }
    }

    static float getFloat(String number) throws Exception {
        out.print(number + ": ");
        return Float.parseFloat(br.readLine());
    }
}
