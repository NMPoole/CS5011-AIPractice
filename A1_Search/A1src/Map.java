/**
 * This class contains the maps to be used for evaluation.
 *
 * @author 170004680
 * @author at258
 */
public enum Map {


    // Test Maps:

    TMAPSmall(new int[][]{ // Added small instance for close testing of the search algorithm implementations.
                {0, 0, 0},
              {0, 1, 0},
            {0, 0, 0}
    }),

    TMAP00(new int[][]{ // TMAP00 is the map in the specification.
                      {0, 0, 0, 0, 0, 0},
                    {0, 0, 2, 0, 0, 0},
                  {0, 0, 2, 0, 0, 0},
                {0, 1, 1, 1, 1, 0},
              {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0}
    }),

    TMAP01(new int[][]{
                  {0, 0, 0, 0},
                {0, 0, 0, 0},
              {0, 0, 0, 0},
            {0, 0, 0, 0},
    }),

    // Maps For Evaluation:

    MAP0(new int[][]{
                      {0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0},
                  {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0}
    }),

    MAP1(new int[][]{
                    {0, 0, 0, 0, 0},
                  {0, 0, 1, 0, 0},
                {0, 0, 2, 0, 0},
              {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
    }),

    MAP2(new int[][]{
                      {0, 0, 0, 0, 0, 0},
                    {0, 1, 1, 1, 0, 0},
                  {0, 1, 0, 1, 0, 0},
                {0, 1, 1, 1, 0, 0},
              {0, 0, 0, 0, 0, 0},
            {0, 0, 2, 2, 2, 0}
    }),

    MAP3(new int[][]{
                      {0, 0, 0, 0, 0, 0},
                    {0, 2, 0, 0, 1, 0},
                  {0, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 1, 0},
              {0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 1, 0}
    }),

    MAP4(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                      {0, 1, 1, 1, 1, 1, 1},
                    {0, 0, 0, 0, 0, 0, 0},
                  {0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 1, 0},
              {0, 2, 2, 2, 2, 2, 0},
            {0, 0, 0, 0, 0, 0, 0}
    });


    private final int[][] map; // Map as 2-D array of integers, to be interpreted as a hexagonal grid.


    /**
     * Constructor: A map is a 2-D array of integers (which has a hexagonal grid interpretation for this practical).
     *
     * @param map Map as 2-D array to assign.
     */
    Map(int[][] map) {
        this.map = map;
    } // Map().

    /**
     * @return Map as int[][].
     */
    public int[][] getMap() {
        return map;
    } // getMap().


} // Map{}.
