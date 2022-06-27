/**
 * This class contains the configurations to be used for evaluation of the search algorithms.
 *
 * @author 170004680
 * @author at258
 */
public enum Conf {


    // TEST Configurations As Discussed In Lectures. TCONF00 is the map in the specification.

    //TCONFFail(Map.TMAPSmall, 0, 0, 1, 1), // Test failure output correct.
    //TCONFSmall(Map.TMAPSmall, 0, 0, 2, 2), // Small problem instance for testing.

    TCONF00(Map.TMAP00, 1, 1, 4, 5),
    TCONF01(Map.TMAP01, 0, 0, 3, 3),

    // Configurations for evaluation.

    CONF0(Map.MAP0, 0, 0, 5, 5),
    CONF1(Map.MAP0, 5, 5, 0, 0),
    CONF2(Map.MAP0, 0, 5, 5, 0),
    CONF3(Map.MAP0, 5, 2, 1, 4),
    CONF4(Map.MAP0, 4, 4, 0, 2),

    CONF5(Map.MAP1, 0, 0, 4, 4),
    CONF6(Map.MAP1, 4, 4, 0, 0),
    CONF7(Map.MAP1, 2, 0, 2, 4),
    CONF8(Map.MAP1, 0, 2, 4, 2),
    CONF9(Map.MAP1, 4, 3, 1, 1),

    CONF10(Map.MAP2, 5, 5, 2, 2),
    CONF11(Map.MAP2, 2, 2, 1, 4),
    CONF12(Map.MAP2, 5, 0, 5, 5),
    CONF13(Map.MAP2, 4, 1, 0, 5),
    CONF14(Map.MAP2, 2, 5, 2, 0),

    CONF15(Map.MAP3, 5, 2, 3, 5),
    CONF16(Map.MAP3, 5, 5, 5, 0),
    CONF17(Map.MAP3, 2, 0, 4, 5),
    CONF18(Map.MAP3, 5, 0, 5, 5),
    CONF19(Map.MAP3, 1, 5, 4, 2),

    CONF20(Map.MAP4, 4, 0, 5, 6),
    CONF21(Map.MAP4, 6, 5, 0, 6),
    CONF22(Map.MAP4, 1, 0, 6, 3),
    CONF23(Map.MAP4, 6, 0, 2, 5),
    CONF24(Map.MAP4, 0, 1, 6, 4);


    private final Map map; // Map to be used in the configuration.
    private final Coord coordP; // Location of person on map (i.e., the start position).
    private final Coord coordS; // Location of safety on map (i.e., the end position).


    /**
     * Constructor:
     *
     * @param map       Map grid to be used (i.e., the search environment).
     * @param coordPRow Row co-ordinate of position P.
     * @param coordPCol Column co-ordinate of position P.
     * @param coordSRow Row co-ordinate of position S.
     * @param coordSCol Column co-ordinate of position S.
     */
    Conf(Map map, int coordPRow, int coordPCol, int coordSRow, int coordSCol) {

        this.map = map; // Search environment, interpreted as a hexagonal grid.
        coordP = new Coord(coordPRow, coordPCol); // Person/Start.
        coordS = new Coord(coordSRow, coordSCol); // Safety/Goal.

    } // Conf().

    /**
     * @return Map as int[][].
     */
    public Map getMap() {
        return map;
    } // getMap().

    /**
     * @return Co-ordinate of P (Person/Start).
     */
    public Coord getCoordP() {
        return coordP;
    } // getCoordP().

    /**
     * @return Co-ordinate of S (Safety/Goal).
     */
    public Coord getCoordS() {
        return coordS;
    } // getCoordS().


} // Conf{}.