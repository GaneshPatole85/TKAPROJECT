package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.TransactionIdgenerator;

import java.util.UUID;
public class GenerateTID {
	public static String processDummyPayment() {
        return UUID.randomUUID().toString();  // Example: "f47ac10b-58cc-4372-a567-0e02b2c3d479"
    }

}
