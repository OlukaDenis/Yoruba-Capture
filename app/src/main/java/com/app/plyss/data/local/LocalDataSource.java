package com.app.plyss.data.local;

import java.util.ArrayList;
import java.util.List;

public class LocalDataSource {
    private static final String TAG = "LocalDataSource";

    public LocalDataSource() {
    }

    public static List<String> ALL_STATES = new ArrayList<>();

    public static final  String[] GENDER = new String[] {"Male", "Female", "Prefer not to say"};

    public static final  String[] RESIDENCE = new String[] {"Owned", "Rented"};

    public static final  String[] MARITAL_STATUS = new String[] {"Single", "Married", "Divorced", "Prefer not to say"};

    public static List<String> getStates() {
        List<String> states = new ArrayList<>();

        states.add("Delta");
        states.add("Edo");
        states.add("Ekiti");
        states.add("Kogi");
        states.add("Plateau");
        states.add("Kwara");
        states.add("Lagos");
        states.add("Ogun");
        states.add("Ondo");
        states.add("Osun");
        states.add("Oyo");

        return states;

    }

    public static List<String> getLGAS(String state) {
        List<String> lgas = new ArrayList<>();

        switch (state) {
            case "Delta":
                lgas.add("Warri North");
                lgas.add("Warri South");
                lgas.add("Warri South West");
                break;

            case "Edo":
                lgas.add("Akoko-Edo");
                break;

            case "Ekiti":
                lgas.add("Ado");
                lgas.add("Efon");
                lgas.add("Ekiti East");
                lgas.add("Ekiti South West");
                lgas.add("Ekiti West");
                lgas.add("Emure");
                lgas.add("Gbonyin");
                lgas.add("Ido-Osi");
                lgas.add("Ijero");
                lgas.add("Ikere");
                lgas.add("Ikole");
                lgas.add("Ilejemeje");
                lgas.add("Irepodun");
                lgas.add("Ise/Orun");
                lgas.add("Moba");
                lgas.add("Oye");
                break;

            case "Kogi":
                lgas.add("Ijumu");
                lgas.add("Kabba/Bunu");
                lgas.add("Lokoja");
                lgas.add("Mopa-Muro");
                lgas.add("Yagba-East");
                lgas.add("Yagba-West");
                break;

            case "Kwara":
                lgas.add("Asa");
                lgas.add("Ekiti");
                lgas.add("Ifelodun");
                lgas.add("Ilori East");
                lgas.add("Ilorin West");
                lgas.add("Ilorin South");
                lgas.add("Irepodun");
                lgas.add("Isin");
                lgas.add("Moro");
                lgas.add("Offa");
                lgas.add("Oke Ero");
                lgas.add("Oyun");
                break;

            case "Lagos":
                lgas.add("Agege");
                lgas.add("Ajeromi-Ifelo");
                lgas.add("Alimosho");
                lgas.add("Amuwodofin");
                lgas.add("Apapa");
                lgas.add("Badagry");
                lgas.add("Epe");
                lgas.add("Eti-Osa");
                lgas.add("Ibeju-Lekki");
                lgas.add("Ifako-Ijaye");
                lgas.add("Ikeja");
                lgas.add("Ikorodu");
                lgas.add("Island");
                lgas.add("Kosofe");
                lgas.add("Mainland");
                lgas.add("Mushin");
                lgas.add("Ojo");
                lgas.add("Oshodi-Isolo");
                lgas.add("Shomolu");
                lgas.add("Surulere");
                break;

            case "Ogun":
                lgas.add("Abeokuta North");
                lgas.add("Abeokuta South");
                lgas.add("Ado-Odo");
                lgas.add("Ewekoro");
                lgas.add("Ifo");
                lgas.add("Ijebu East");
                lgas.add("Ijebu North");
                lgas.add("Ijebu North East");
                lgas.add("Ijebu Ode");
                lgas.add("Ikenne");
                lgas.add("Imeko Afon");
                lgas.add("Ipokia");
                lgas.add("Owode");
                lgas.add("Odeda");
                lgas.add("Odogbolu");
                lgas.add("Waterside");
                lgas.add("Remo North");
                lgas.add("Sagamu");
                lgas.add("Yewa North");
                lgas.add("Yewa South");
                break;

            case "Ondo":
                lgas.add("Akoko North East");
                lgas.add("Akoko North West");
                lgas.add("Akoko South West");
                lgas.add("Akure North");
                lgas.add("Akure South");
                lgas.add("Ese Odo");
                lgas.add("Idanre");
                lgas.add("Ifedore");
                lgas.add("Ilaje");
                lgas.add("Ile Oluji");
                lgas.add("Irele");
                lgas.add("Odigbo");
                lgas.add("Okitipupa");
                lgas.add("Ondo East");
                lgas.add("Ondo West");
                lgas.add("Ose");
                lgas.add("Owo");
                break;

            case "Osun":
                lgas.add("Aiyedaade");
                lgas.add("Aiyedire");
                lgas.add("Atakunmosa East");
                lgas.add("Atakunmosa West");
                lgas.add("Boluwaduro");
                lgas.add("Boripe");
                lgas.add("Ede North");
                lgas.add("Ede South");
                lgas.add("Egbedore");
                lgas.add("Ejigbo");
                lgas.add("Ife Central");
                lgas.add("Ife East");
                lgas.add("Ife North");
                lgas.add("Ife South");
                lgas.add("Ifedayo");
                lgas.add("Ifelodun");
                lgas.add("Ila");
                lgas.add("Ilesa East");
                lgas.add("Ilesa West");
                lgas.add("Irepodun");
                lgas.add("Irewole");
                lgas.add("Isokan");
                lgas.add("Iwo");
                lgas.add("Obokun");
                lgas.add("Odo Otin");
                lgas.add("Ola Oluwa");
                lgas.add("Olorunda");
                lgas.add("Oriade");
                lgas.add("Orolu");
                break;

            case "Oyo":
                lgas.add("Afijio");
                lgas.add("Akinyele");
                lgas.add("Atiba");
                lgas.add("Atisbo");
                lgas.add("Egbeda");
                lgas.add("Ibadan North");
                lgas.add("Ibanda North East");
                lgas.add("Ibadan North West");
                lgas.add("Ibadan South West");
                lgas.add("Ibarapa Central");
                lgas.add("Ibarapa East");
                lgas.add("Ibarapa North");
                lgas.add("Ido");
                lgas.add("Irepo");
                lgas.add("Iseyin");
                lgas.add("Itesiwaju");
                lgas.add("Iwajowa");
                lgas.add("Kajola");
                lgas.add("Lagelu");
                lgas.add("Ogbomoso North");
                lgas.add("Ogbomoso South");
                lgas.add("Ogo Oluwa");
                lgas.add("Olorunsogo");
                lgas.add("Oluyole");
                lgas.add("Ona Ara");
                lgas.add("Orelope");
                lgas.add("Ori Ire");
                lgas.add("Oyo East");
                lgas.add("Oyo West");
                lgas.add("Saki East");
                lgas.add("Saki West");
                lgas.add("Surulere");
                break;

            case "Plateau":
                lgas.add("Barkin Ladi");
                lgas.add("Bassa");
                lgas.add("Bokkos");
                lgas.add("Jos East");
                lgas.add("Jos North");
                lgas.add("Jos South");
                lgas.add("Kanam");
                lgas.add("Kanke");
                lgas.add("Langtang North");
                lgas.add("Langtang South");
                lgas.add("Mangu");
                lgas.add("Mikang");
                lgas.add("Pankshin");
                lgas.add("Qua'an Pan");
                lgas.add("Riyom");
                lgas.add("Shendam");
                lgas.add("Wase");
                break;

            default:
                lgas.add("Empty State");

        }

        return lgas;
    }
}
