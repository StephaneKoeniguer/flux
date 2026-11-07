package fr.flux;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.TorrentStatus;
import com.frostwire.jlibtorrent.alerts.*;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import com.frostwire.jlibtorrent.TorrentFlags;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MoteurTorrent {

    private final SessionManager session = new SessionManager();
    private final ScheduledExecutorService horloge = Executors.newSingleThreadScheduledExecutor();
    private final File dossierTelechargements;
    private volatile Consumer<List<EtatTorrent>> ecouteur = etats -> {};

    public MoteurTorrent(File dossierTelechargements) {
        this.dossierTelechargements = dossierTelechargements;
    }

    public void demarrer() {
        dossierTelechargements.mkdirs();

        session.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return new int[] {
                        AlertType.ADD_TORRENT.swig(),
                        AlertType.STATE_UPDATE.swig(),
                        AlertType.METADATA_RECEIVED.swig(),
                        AlertType.TORRENT_FINISHED.swig()
                };
            }

            @Override
            public void alert(Alert<?> alert) {
                traiterAlerte(alert);
            }
        });

        session.start();

        horloge.scheduleAtFixedRate(session::postTorrentUpdates, 1, 1, TimeUnit.SECONDS);

        log("Moteur démarré. Téléchargements dans " + dossierTelechargements);
    }

    public void ajouterMagnet(String lien) {
        log("Ajout demandé");
        session.download(lien, dossierTelechargements, new torrent_flags_t());
    }

    public void arreter() {
        horloge.shutdown();
        session.stop();
        log("Moteur arrêté");
    }

    private void traiterAlerte(Alert<?> alert) {
        switch (alert.type()) {
            case ADD_TORRENT -> {
                AddTorrentAlert ajout = (AddTorrentAlert) alert;
                ajout.handle().resume();
                log("Ajouté : " + ajout.torrentName());
            }
            case STATE_UPDATE -> {
                List<EtatTorrent> etats = new ArrayList<>();
                for (TorrentStatus status : ((StateUpdateAlert) alert).status()) {
                    etats.add(convertir(status));
                }
                ecouteur.accept(etats);
            }
            case METADATA_RECEIVED -> log("Infos reçues : " + ((MetadataReceivedAlert) alert).torrentName());
            case TORRENT_FINISHED -> log("Terminé : " + ((TorrentFinishedAlert) alert).torrentName());
            default -> {
                // les autres alertes ne nous intéressent pas pour l'instant
            }
        }
    }

    public void surMiseAJour(Consumer<List<EtatTorrent>> ecouteur) {
        this.ecouteur = ecouteur;
    }

    private static EtatTorrent convertir(TorrentStatus status) {
        return new EtatTorrent(
                status.infoHash().toHex(), // identifiant unique
                status.name(),
                status.totalWanted(),
                status.progress(),
                statutDe(status),
                status.downloadPayloadRate(),
                status.uploadPayloadRate());
    }

    private static Statut statutDe(TorrentStatus status) {
        // Être en pause est un "drapeau" à part de l'état
        if (status.flags().and_(TorrentFlags.PAUSED).nonZero()) {
            return Statut.PAUSE;
        }
        return switch (status.state()) {
            case DOWNLOADING_METADATA -> Statut.RECHERCHE;
            case CHECKING_FILES, CHECKING_RESUME_DATA -> Statut.VERIFICATION;
            case SEEDING -> Statut.PARTAGE;
            case FINISHED -> Statut.TERMINE;
            default -> Statut.TELECHARGEMENT;
        };
    }

    // Affiche le message avec le nom du thread qui l'exécute
    private static void log(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + message);
    }

}
