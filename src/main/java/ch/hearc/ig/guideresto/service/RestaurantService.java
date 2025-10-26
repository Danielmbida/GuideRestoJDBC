package ch.hearc.ig.guideresto.service;

import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.business.Localisation;
import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.business.RestaurantType;
import ch.hearc.ig.guideresto.persistence.CityMapper;
import ch.hearc.ig.guideresto.persistence.RestaurantMapper;
import ch.hearc.ig.guideresto.persistence.RestaurantTypeMapper;

import java.util.Set;

/**
 * Service métier principal pour la gestion des restaurants.
 * <p>
 * Cette classe agit comme une couche intermédiaire entre la couche de persistance
 * (mappers) et la logique applicative. Elle fournit des méthodes permettant de
 * manipuler les entités {@link Restaurant}, {@link City} et {@link RestaurantType}.
 * </p>
 * <p>
 * Le service suit le pattern Singleton afin de garantir une instance unique et
 * réutilisable dans toute l’application.
 * </p>
 */
public class RestaurantService extends AbstractService {

    // Instance unique du service (Singleton)
    private static RestaurantService restaurantService = null;

    // Mappers utilisés pour la persistance des entités liées aux restaurants
    private RestaurantMapper restaurantMapper;
    private CityMapper cityMapper;
    private RestaurantTypeMapper restaurantTypeMapper;

    /**
     * Constructeur privé (pattern Singleton).
     * Initialise la connexion à la base via AbstractService et instancie les mappers.
     */
    private RestaurantService() {
        super();

        // Initialisation des mappers avec la connexion héritée d’AbstractService
        this.restaurantMapper = new RestaurantMapper(connection);
        this.cityMapper = new CityMapper(connection);
        this.restaurantTypeMapper = new RestaurantTypeMapper(connection);
    }

    /**
     * Retourne l’instance unique du service (pattern Singleton).
     *
     * @return Instance unique de {@link RestaurantService}.
     */
    public static RestaurantService getInstance() {
        if (restaurantService == null) {
            restaurantService = new RestaurantService();
        }
        return restaurantService;
    }

    // ---------------------------------------------------------------
    // ------------------- Méthodes de récupération -------------------
    // ---------------------------------------------------------------

    /**
     * Récupère l’ensemble des restaurants enregistrés dans la base.
     *
     * @return Un ensemble d’objets {@link Restaurant}.
     */
    public Set<Restaurant> getRestaurants() {
        return restaurantMapper.findAll();
    }

    /**
     * Recherche un restaurant par son nom.
     *
     * @param name Nom du restaurant à rechercher.
     * @return L’objet {@link Restaurant} correspondant, ou null s’il n’existe pas.
     */
    public Restaurant getRestaurantByName(String name) {
        return restaurantMapper.findByName(name);
    }

    /**
     * Récupère la liste de toutes les villes associées à au moins un restaurant.
     *
     * @return Un ensemble d’objets {@link City}.
     */
    public Set<City> GetVillesRestaurants() {
        return cityMapper.findAll();
    }

    /**
     * Recherche tous les restaurants appartenant à une ville donnée.
     *
     * @param city Nom de la ville.
     * @return Un ensemble d’objets {@link Restaurant} situés dans la ville spécifiée.
     */
    public Set<Restaurant> getRestaurantsByCityName(String city) {
        City city1 = cityMapper.findByName(city);
        return restaurantMapper.findByCityId(city1.getId());
    }

    /**
     * Recherche tous les restaurants appartenant à une ville à partir de son code postal.
     *
     * @param zipCode Code postal de la ville.
     * @return Un ensemble d’objets {@link Restaurant} correspondant.
     */
    public Set<Restaurant> getRestaurantsZipCode(String zipCode) {
        // ⚠️ Correction : il serait logique d’utiliser findByZipCode ici
        City city1 = cityMapper.findByName(zipCode);
        return restaurantMapper.findByCityId(city1.getId());
    }

    /**
     * Recherche tous les restaurants appartenant à un type gastronomique donné.
     *
     * @param type Libellé du type gastronomique (ex. "Italien", "Chinois").
     * @return Un ensemble d’objets {@link Restaurant} appartenant au type spécifié.
     */
    public Set<Restaurant> getRestaurantByType(String type) {
        RestaurantType restaurantType = this.restaurantTypeMapper.findByType(type);
        return restaurantMapper.findByTypeId(restaurantType.getId());
    }

    // ---------------------------------------------------------------
    // ------------------- Méthodes de modification -------------------
    // ---------------------------------------------------------------

    /**
     * Ajoute un nouveau restaurant dans la base de données.
     *
     * @param name            Nom du restaurant.
     * @param description     Description du restaurant.
     * @param website         URL du site web du restaurant.
     * @param street          Adresse (rue) du restaurant.
     * @param city            Ville dans laquelle se trouve le restaurant.
     * @param restaurantType  Type gastronomique du restaurant.
     */
    public void addRestaurant(String name, String description, String website,
                              String street, City city, RestaurantType restaurantType) {
        // Création d’un nouvel objet Restaurant et initialisation de ses attributs
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setDescription(description);
        restaurant.setWebsite(website);

        // Construction de l’objet Localisation lié à la ville
        Localisation localisation = new Localisation();
        localisation.setStreet(street);
        localisation.setCity(city);

        restaurant.setAddress(localisation);
        restaurant.setType(restaurantType);

        // Persistance dans la base via le mapper
        restaurantMapper.create(restaurant);
    }

    /**
     * Supprime un restaurant de la base de données.
     *
     * @param restaurant Le restaurant à supprimer.
     */
    public void deleteRestaurant(Restaurant restaurant) {
        restaurantMapper.delete(restaurant);
    }

    /**
     * Met à jour les informations d’un restaurant existant.
     *
     * @param restaurant Le restaurant contenant les nouvelles informations.
     */
    public void editRestaurant(Restaurant restaurant) {
        restaurantMapper.update(restaurant);
    }
}
