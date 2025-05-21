// Ucak.java
public class Ucak {
    private String model;
    private String marka;
    private String seriNo;
    private int koltukKapasitesi;

    public Ucak(String model, String marka, String seriNo, int koltukKapasitesi) {
        this.model = model;
        this.marka = marka;
        this.seriNo = seriNo;
        this.koltukKapasitesi = koltukKapasitesi;
    }

    public String getModel() { return model; }
    public String getMarka() { return marka; }
    public String getSeriNo() { return seriNo; }
    public int getKoltukKapasitesi() { return koltukKapasitesi; }

    public String toString() {
        return marka + " " + model + " [" + seriNo + "] - Kapasite: " + koltukKapasitesi;
    }
}

// Lokasyon.java
public class Lokasyon {
    private String ulke;
    private String sehir;
    private String havaalani;
    private boolean aktif;

    public Lokasyon(String ulke, String sehir, String havaalani, boolean aktif) {
        this.ulke = ulke;
        this.sehir = sehir;
        this.havaalani = havaalani;
        this.aktif = aktif;
    }

    public String getUlke() { return ulke; }
    public String getSehir() { return sehir; }
    public String getHavaalani() { return havaalani; }
    public boolean isAktif() { return aktif; }

    public String toString() {
        return havaalani + " (" + sehir + ", " + ulke + ") - " + (aktif ? "Aktif" : "Pasif");
    }
}

// Ucus.java
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Ucus {
    private Lokasyon lokasyon;
    private LocalDateTime saat;
    private Ucak ucak;
    private ArrayList<Rezervasyon> rezervasyonlar = new ArrayList<>();

    public Ucus(Lokasyon lokasyon, LocalDateTime saat, Ucak ucak) {
        this.lokasyon = lokasyon;
        this.saat = saat;
        this.ucak = ucak;
    }

    public boolean rezerveEt(Rezervasyon r) {
        if (rezervasyonlar.size() < ucak.getKoltukKapasitesi()) {
            rezervasyonlar.add(r);
            return true;
        }
        return false;
    }

    public Lokasyon getLokasyon() { return lokasyon; }
    public LocalDateTime getSaat() { return saat; }
    public Ucak getUcak() { return ucak; }
    public ArrayList<Rezervasyon> getRezervasyonlar() { return rezervasyonlar; }

    public String toString() {
        return lokasyon + " - Saat: " + saat + " | " + ucak;
    }
}

// Rezervasyon.java
public class Rezervasyon {
    private Ucus ucus;
    private String ad;
    private String soyad;
    private int yas;

    public Rezervasyon(Ucus ucus, String ad, String soyad, int yas) {
        this.ucus = ucus;
        this.ad = ad;
        this.soyad = soyad;
        this.yas = yas;
    }

    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }
    public int getYas() { return yas; }
    public Ucus getUcus() { return ucus; }

    public String toString() {
        return ad + " " + soyad + " (" + yas + " yaþýnda) - " + ucus.toString();
    }
}

// DosyaYazici.java
public interface DosyaYazici {
    void yazRezervasyon(Rezervasyon rezervasyon);
    void yazUcak(Ucak ucak);
    void yazLokasyon(Lokasyon lokasyon);
    void yazUcus(Ucus ucus);
}

// JsonYazici.java
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class JsonYazici implements DosyaYazici {
    private void dosyayaYaz(String dosyaAdi, JSONObject obj) {
        try (FileWriter file = new FileWriter(dosyaAdi, true)) {
            file.write(obj.toJSONString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void yazRezervasyon(Rezervasyon r) {
        JSONObject obj = new JSONObject();
        obj.put("ad", r.getAd());
        obj.put("soyad", r.getSoyad());
        obj.put("yas", r.getYas());
        obj.put("ucus", r.getUcus().toString());
        dosyayaYaz("rezervasyonlar.json", obj);
    }

    public void yazUcak(Ucak u) {
        JSONObject obj = new JSONObject();
        obj.put("model", u.getModel());
        obj.put("marka", u.getMarka());
        obj.put("seriNo", u.getSeriNo());
        obj.put("kapasite", u.getKoltukKapasitesi());
        dosyayaYaz("ucaklar.json", obj);
    }

    public void yazLokasyon(Lokasyon l) {
        JSONObject obj = new JSONObject();
        obj.put("ulke", l.getUlke());
        obj.put("sehir", l.getSehir());
        obj.put("havaalani", l.getHavaalani());
        obj.put("aktif", l.isAktif());
        dosyayaYaz("lokasyonlar.json", obj);
    }

    public void yazUcus(Ucus u) {
        JSONObject obj = new JSONObject();
        obj.put("lokasyon", u.getLokasyon().toString());
        obj.put("saat", u.getSaat().toString());
        obj.put("ucak", u.getUcak().toString());
        dosyayaYaz("ucuslar.json", obj);
    }
}

// Main.java
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Ucus> ucuslar = new ArrayList<>();
        DosyaYazici yazici = new JsonYazici();

        Lokasyon lokasyon1 = new Lokasyon("Türkiye", "Ýstanbul", "IST", true);
        Ucak ucak1 = new Ucak("737", "Boeing", "ABC123", 2);
        Ucus ucus1 = new Ucus(lokasyon1, LocalDateTime.of(2025, 6, 1, 10, 0), ucak1);

        yazici.yazLokasyon(lokasyon1);
        yazici.yazUcak(ucak1);
        yazici.yazUcus(ucus1);

        ucuslar.add(ucus1);

        System.out.println("Mevcut Uçuþlar:");
        for (int i = 0; i < ucuslar.size(); i++) {
            System.out.println((i + 1) + ". " + ucuslar.get(i));
        }

        System.out.print("Uçuþ seçiniz (1-" + ucuslar.size() + "): ");
        int secim = sc.nextInt(); sc.nextLine();

        Ucus secilenUcus = ucuslar.get(secim - 1);

        System.out.print("Adýnýz: ");
        String ad = sc.nextLine();
        System.out.print("Soyadýnýz: ");
        String soyad = sc.nextLine();
        System.out.print("Yaþýnýz: ");
        int yas = sc.nextInt();

        Rezervasyon r = new Rezervasyon(secilenUcus, ad, soyad, yas);
        boolean basarili = secilenUcus.rezerveEt(r);

        if (basarili) {
            System.out.println("Rezervasyon baþarýlý!");
            yazici.yazRezervasyon(r);
        } else {
            System.out.println("Maalesef bu uçuþta boþ koltuk yok.");
        }
    }
}
