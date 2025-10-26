package ch.hearc.ig.guideresto.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class AbstractService {
    protected static final Logger logger = LogManager.getLogger();
    protected Connection connection;
    public AbstractService() {
        try{
            // Chargement du fichier de configuration depuis le classpath
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties");

            if (input == null) {
                throw new RuntimeException("Fichier database.properties introuvable dans le classpath !");
            }

            props.load(input);

            // Ouverture de la connexion JDBC
            this.connection = DriverManager.getConnection(
                    props.getProperty("database.url"),
                    props.getProperty("database.username"),
                    props.getProperty("database.password")
            );

        }catch(Exception e){
            logger.error("Erreur lors de la connection à la bd : " + e.getMessage());
        }

    }
}
