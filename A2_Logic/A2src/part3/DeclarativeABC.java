package part3;

import org.logicng.datastructures.Assignment;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.formulas.Variable;
import org.logicng.solvers.MiniSat;
import org.logicng.datastructures.Tristate;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;

import mains.Grid;
import mains.Puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DeclarativeABC {

    private static final String NOT = "~";
    private static final String OR = "|";
    private static final String AND = "&";

    private Puzzle puzzle;
    private Grid grid;

    FormulaFactory formulaFactory;
    PropositionalParser propositionalParser;
    MiniSat solver;
    Tristate result;

    HashMap<Integer, HashSet<Variable>> rowsToLiterals;
    HashMap<Integer, HashSet<Variable>> colsToLiterals;
    HashMap<Character, HashSet<Variable>> symbolsToLiterals;

    HashSet<Formula> clauses;


    /**
     * Empty Constructor:
     */
    public DeclarativeABC() {
        // Empty.
    } // DeclarativeABC().

    /**
     * Constructor: Create an instance of DeclarativeABD.
     *
     * @param puzzle The puzzle object for the 'Easy as ABC' instance.
     */
    public DeclarativeABC(Puzzle puzzle) {
        this.setup(puzzle);
    } // DeclarativeABC().

    /**
     * Constructor: Create an instance of DeclarativeABD.
     *
     * @param puzzle The puzzle object for the 'Easy as ABC' instance.
     * @param grid The grid of characters for the puzzle.
     */
    public DeclarativeABC(Puzzle puzzle, Grid grid) {
        this.setup(puzzle, grid);
    } // DeclarativeABC().

    /**
     * @return Return whether result is unknown, it is unknown if there is a solution.
     */
    public boolean isUnknown() {
        return result == Tristate.UNDEF;
    } // isUnknown().

    /**
     * @return Return whether result is true, so there is a solution.
     */
    public boolean isTrue() {
        return result == Tristate.TRUE;
    } // isTrue().

    /**
     * @return Return whether result is false, so there is no solution.
     */
    public boolean isFalse() {
        return result == Tristate.FALSE;
    }

    /**
     * Sets up any necessary data structures.
     *
     * @param puzzle puzzle for the problem.
     */
    public void setup(Puzzle puzzle) {
        this.setup(puzzle, new Grid(puzzle.size()));
    }

    /**
     * Sets up any necessary data structures.
     *
     * @param puzzle Puzzle object for the 'Easy as ABC' problem.
     * @param grid Grid object for the 'Easy as ABC' problem.
     */
    public void setup(Puzzle puzzle, Grid grid) {

        this.puzzle = puzzle;
        this.grid = grid;

        this.formulaFactory = new FormulaFactory();
        this.propositionalParser = new PropositionalParser(formulaFactory);

        clauses = new HashSet<>();

        // Create propositional variable data structures for the puzzle.

        rowsToLiterals = new HashMap<>();
        colsToLiterals = new HashMap<>();
        for (int index = 0; index < grid.size(); index++) {
            rowsToLiterals.put(index, new HashSet<>());
            colsToLiterals.put(index, new HashSet<>());
        }

        symbolsToLiterals = new HashMap<>();
        symbolsToLiterals.put(Puzzle.UNFILLED_CHAR, new HashSet<>());
        for (Character currSymbol : puzzle.symbols) {
            symbolsToLiterals.put(currSymbol, new HashSet<>());
        }

        // Create propositional variables for the puzzle.
        createPropositionalVariables();

        this.solver = MiniSat.miniSat(formulaFactory);
        this.result = Tristate.UNDEF;

    } // setup().

    /**
     * Create a set of propositional variables required for the problem.
     */
    public HashSet<String> createPropositionalVariables() {

        HashSet<String> variableNames = new HashSet<>();

        // For all squares in the grid...
        for (int rowIndex = 0; rowIndex < grid.size(); rowIndex++) {
            for (int colIndex = 0; colIndex < grid.size(); colIndex++) {

                // Create variables for every symbol (letters + BLANK).
                for (char currSymbol : puzzle.symbols) {

                    String currVariableName = rowIndex + String.valueOf(colIndex) + currSymbol;
                    Variable currSymbolVariable = formulaFactory.variable(currVariableName);

                    // Add to structures accordingly.
                    rowsToLiterals.get(rowIndex).add(currSymbolVariable);
                    colsToLiterals.get(colIndex).add(currSymbolVariable);
                    symbolsToLiterals.get(currSymbol).add(currSymbolVariable);

                    variableNames.add(currVariableName);

                }

            }
        }

        return variableNames; // For testing purposes only.

    } // createPropositionalVariables().

    /**
     * Create CNF clauses for this 'Easy as ABC' puzzle.
     */
    public void createClauses() {

        cellConstraints();
        rowConstraints();
        colConstraints();
        clueConstraints();

    } // createClauses().

    /**
     * Adds CNF constraints to represent the cell/square rules in the puzzle.
     */
    private void cellConstraints() {

        everySquareAtLeastOneSymbol();
        everySquareMaxOneSymbol();

    } // cellConstraints().

    /**
     * Create CNF clauses which enforce that every square requires at least one symbol.
     */
    public HashSet<Formula> everySquareAtLeastOneSymbol() {

        HashSet<Formula> addedClauses = new HashSet<>();

        // For every square in the grid...
        for (int rowIndex = 0; rowIndex < grid.size(); rowIndex++) {
            for (int colIndex = 0; colIndex < grid.size(); colIndex++) {

                // Get variables related to current row.
                HashSet<Variable> literalsFromRows = rowsToLiterals.get(rowIndex);
                // Get variables related to current column.
                HashSet<Variable> literalsFromCols = colsToLiterals.get(colIndex);

                // Intersection is variables related to this square.
                HashSet<Variable> squareVariables = new HashSet<>(literalsFromRows); // Use the copy constructor.
                squareVariables.retainAll(literalsFromCols);

                // Clause is the disjunction of all variables at the given row, given column, and each symbol.
                // Each entry has at least one value: 𝐶1 = BigAnd1≤𝑟≤𝑛,1≤𝑐≤𝑛(x𝑟𝑐1 ∨ x𝑟𝑐2 ∨ … ∨ x𝑟𝑐k).
                Formula clause = formulaFactory.clause(squareVariables);

                clauses.add(clause);
                addedClauses.add(clause);

            }
        }

        return addedClauses; // For testing purposes only.

    } // everySquareAtLeastOneSymbol().

    /**
     * Create CNF clauses which enforce that every square requires at most one symbol.
     */
    public HashSet<Formula> everySquareMaxOneSymbol() {

        HashSet<Formula> clausesAdded = new HashSet<>();

        // For every square in the grid...
        for (int rowIndex = 0; rowIndex < grid.size(); rowIndex++) {
            for (int colIndex = 0; colIndex < grid.size(); colIndex++) {

                // Get variables related to current row.
                HashSet<Variable> literalsFromRows = rowsToLiterals.get(rowIndex);
                // Get variables related to current column.
                HashSet<Variable> literalsFromCols = colsToLiterals.get(colIndex);

                // Intersection is variables related to this square.
                ArrayList<Variable> squareVariables = new ArrayList<>(literalsFromRows); // Use the copy constructor.
                squareVariables.retainAll(literalsFromCols);

                // Create clause: for each square, the square contains at most one symbol.
                for (int i = 0; i < squareVariables.size(); i++) {
                    for (int j = i + 1; j < squareVariables.size(); j++) {

                        Variable squareVariableI = squareVariables.get(i);
                        Variable squareVariableJ = squareVariables.get(j);

                        // Each entry has at most one value: 𝐶2 = BigAnd1≤𝑟≤𝑛,1≤𝑐≤𝑛,1≤𝑣<𝑣′≤k(¬x𝑟𝑐𝑣 ∨ ¬x𝑟𝑐𝑣′).
                        // String clause = NOT + squareVariableI.name() + OR + NOT + squareVariableJ.name();
                        Formula clause = formulaFactory.or(formulaFactory.not(squareVariableI), formulaFactory.not(squareVariableJ));

                        // Add clause to set of clauses.
                        clauses.add(clause);
                        clausesAdded.add(clause);

                    }
                }

            }
        }

        return clausesAdded; // For testing purposes only.

    } // everySquareMaxOneSymbol().

    /**
     * Adds CNF constraints to represent the row rules in the puzzle.
     */
    private void rowConstraints() {

        // Every row must have every letter at least once.
        rowEveryLetterAtLeastOnce();

        // Every row must have every letter at most once.
        rowEveryLetterAtMostOnce();

        // Every row will have one or more blank squares unless the number of letters is the same as the size of the grid.
        //rowNumBlanks(); // Not required.

    } // rowConstraints().

    /**
     * Adds CNF constraints to represent the column rules in the puzzle.
     */
    private void colConstraints() {

        // Every column must have every letter at least once.
        colEveryLetterAtLeastOnce();

        // Every column must have every letter at most once.
        colEveryLetterAtMostOnce();

        // Every column will have one or more blank squares unless the number of letters is the same as the size of the grid.
        //colNumBlanks(); // Not required.

    } // colConstraints().

    /**
     * Create CNF constraints to enforce that every row must have every letter at least once.
     */
    public HashSet<Formula> rowEveryLetterAtLeastOnce() {

        HashSet<Formula> clausesAdded = everyLetterAtLeastOnce(true);
        return clausesAdded; // For testing purposes only.

    } // rowEveryLetterAtLeastOnce().

    /**
     * Create CNF constraints to enforce that every column must have every letter at least once.
     */
    public HashSet<Formula> colEveryLetterAtLeastOnce() {

        HashSet<Formula> addedClauses = everyLetterAtLeastOnce(false);
        return addedClauses;

    } // colEveryLetterAtLeastOnce().

    /**
     * Create CNF constraints to enforce that every row/column must have every letter at least once.
     *
     * @param isRow Whether enforcing constraint specifically on rows or columns.
     */
    private HashSet<Formula> everyLetterAtLeastOnce(boolean isRow) {

        HashSet<Formula> addedClauses = new HashSet<>();

        // For every row/column in the grid...
        for (int index = 0; index < grid.size(); index++) {
            // For every letter...
            for (char currLetter : puzzle.letters) {

                // Get variables related to current row and column.
                HashSet<Variable> literals;
                if (isRow) {
                    literals = rowsToLiterals.get(index);
                } else {
                    literals = colsToLiterals.get(index);
                }

                // Get variables related to current letter.
                HashSet<Variable> literalsFromSymbols = symbolsToLiterals.get(currLetter);

                // Intersection is variables related to this row/column and letter combination.
                HashSet<Variable> letterVariables = new HashSet<>(literals); // Use the copy constructor.
                letterVariables.retainAll(literalsFromSymbols);

                // Rows: Each entry has at least one value: 𝐶3 = BigAnd1≤r≤n,1≤v≤k(xr1v ∨ xr2v ∨ … ∨ xrnv).
                // Cols: Each entry has at least one value: 𝐶4 = BigAnd1≤c≤n,1≤v≤k(x1cv ∨ xr2cv ∨ … ∨ xncv).
                Formula clause = formulaFactory.clause(letterVariables);

                // Add clause to set of clauses.
                clauses.add(clause);
                addedClauses.add(clause);

            }

        }

        return addedClauses; // For testing purposes only.

    } // everyLetterAtLeastOnce().

    /**
     * Create CNF constraints to enforce that every row must have every letter at most once.
     */
    public HashSet<Formula> rowEveryLetterAtMostOnce() {

        HashSet<Formula> addedClauses = everyLetterAtMostOnce(true);
        return addedClauses;

    } // rowEveryLetterAtMostOnce().

    /**
     * Create CNF constraints to enforce that every column must have every letter at most once.
     */
    public HashSet<Formula> colEveryLetterAtMostOnce() {

        HashSet<Formula> addedClauses = everyLetterAtMostOnce(false);
        return addedClauses;

    } // colEveryLetterAtMostOnce().

    /**
     * Create CNF constraints to enforce that every row/column must have every letter at most once.
     *
     * @param isRow Whether enforcing constraint specifically on rows or columns.
     */
    private HashSet<Formula> everyLetterAtMostOnce(boolean isRow) {

        HashSet<Formula> addedClauses = new HashSet<>();

        // For every square in the grid...
        for (int index = 0; index < grid.size(); index++) {
            // For every letter...
            for (char currLetter : puzzle.letters) {

                HashSet<Variable> literals;
                if (isRow) {
                    // Get variables related to current row.
                    literals = rowsToLiterals.get(index);
                } else {
                    // Get variables related to current column.
                    literals = colsToLiterals.get(index);
                }

                // Get variables related to current letter.
                HashSet<Variable> literalsFromSymbols = symbolsToLiterals.get(currLetter);

                // Intersection is variables related to this row/column and letter.
                ArrayList<Variable> letterVariables = new ArrayList<>(literals); // Use the copy constructor.
                letterVariables.retainAll(literalsFromSymbols);

                // Create clause: for each row/column, the row/column contains at most one of every letter.
                for (int i = 0; i < letterVariables.size(); i++) {
                    for (int j = i + 1; j < letterVariables.size(); j++) {

                        Variable variableI = letterVariables.get(i);
                        Variable variableJ = letterVariables.get(j);

                        // For Rows: Each entry has at most one value: 𝐶 = BigAnd1≤r≤𝑛,1≤v≤k,1≤c<c′≤𝑛(¬xrcv ∨ ¬xrc'v).
                        // For Cols: Each entry has at most one value: 𝐶 = BigAnd1≤c≤𝑛,1≤v≤k,1≤r<r′≤𝑛(¬xrcv ∨ ¬xr'cv).
                        // String clause = NOT + variableI.name() + OR + NOT + variableJ.name();
                        Formula clause = formulaFactory.or(formulaFactory.not(variableI), formulaFactory.not(variableJ));

                        // Add clause to set of clauses.
                        clauses.add(clause);
                        addedClauses.add(clause);

                    }
                }

            }
        }

        return addedClauses;

    } // everyLetterAtMostOnce().

    /**
     * Create CNF constraints to enforce rules about clues.
     *
     * Where a clue is given in a row/column, the first symbol in that row/column direction is consistent with the
     * clue, i.e. the first non blank symbol is the clued letter. If no clue is provided, no restriction applies.
     */
    private void clueConstraints() {

        cluesAdheredForRows();
        cluesAdheredForCols();

    } // clueConstraints().

    /**
     * Create CNF constraints to enforce that clues have to be adhered to on rows.
     */
    public HashSet<Formula> cluesAdheredForRows() {

        HashSet<Formula> addedClauses = cluesAdhered(true, false); // Left clues.
        HashSet<Formula> addedClauses2 = cluesAdhered(true, true); // Right clues.

        addedClauses.addAll(addedClauses2);
        return addedClauses; // For testing purposes only.

    } // cluesForRows().

    /**
     * Create constraints to enforce that clues have to be adhered to on columns.
     */
    public HashSet<Formula> cluesAdheredForCols() {

        HashSet<Formula> addedClauses = cluesAdhered(false, false); // Top clues.
        HashSet<Formula> addedClauses2 = cluesAdhered(false, true); // Bottom clues.

        addedClauses.addAll(addedClauses2);
        return addedClauses; // For testing purposes only.

    } // cluesForCols().

    /**
     * Create constraints to enforce that clues (top, bottom, left, and right) have to be adhered to on all rows/columns.
     *
     * @param isRow Whether the constraint apply to rows, or columns.
     * @param countDown Whether index counts up (top and left clues) or down (bottom and right) clues.
     */
    private HashSet<Formula> cluesAdhered(boolean isRow, boolean countDown) {

        HashSet<Formula> addedClauses = new HashSet<>();

        // Initialise counting correctly. For top/left clues the indices count up, and vice versa for bottom/right clues.
        int startIndex = 0;
        int lastIndex = grid.size() - 1;
        int increment = 1;

        if (countDown) {
            startIndex = grid.size() - 1;
            lastIndex = 0;
            increment = -1;
        }

        // Loop over all rows/columns either counting up or down as needed for the clue set.
        for (int index = startIndex; (!countDown) ? index <= lastIndex : index >= lastIndex; index += increment) {

            // Get the clue that needs to be adhered to for this row/column.
            char clue;
            if (!isRow && !countDown) { // Top clues.
                clue = puzzle.top[index];
            } else if (!isRow) { // Bottom clues.
                clue = puzzle.bottom[index];
            } else if (!countDown) { // Left clues.
                clue = puzzle.left[index];
            } else { // Right clues.
                clue = puzzle.right[index];
            }

            if (clue == Puzzle.UNFILLED_CHAR) { // No constraints for unfilled clues.
                continue;
            }

            // Take advantage of known max number of blanks that can occur before the clue letter must occur in row/column.
            int maxBlanks = grid.size() - puzzle.numLetters();
            int count = startIndex;

            StringBuilder formula = new StringBuilder();
            String newFormulaAddition = "";
            do {

                String currClueVariable; // Clue variable name.
                if (isRow) {
                    currClueVariable = index + (count + String.valueOf(clue));
                } else {
                    currClueVariable = count + (index + String.valueOf(clue));
                }

                if (count == startIndex) { // Initialise the formula at the start.

                    formula = new StringBuilder(currClueVariable); // Clue letter occurs immediately next to clue in grid.
                    newFormulaAddition = currClueVariable;

                } else { // Clue occurs in incrementing positions away from the clue letter, with blanks between.

                    // Adds new option where clue letter is one position further away from the clue and blanks between.
                    newFormulaAddition = newFormulaAddition.replace(String.valueOf(clue), String.valueOf(Puzzle.BLANK_SYMBOL))
                            + AND + currClueVariable;

                    // Add to the formula.
                    formula.append(OR).append("(").append(newFormulaAddition).append(")");

                }

                count += increment;

            } while ((!countDown) ? count < maxBlanks + 1 : count >= grid.size() - 1 - maxBlanks);
            // The condition above ensures that we never allow a clue letter to occur farther away than the number of
            // blanks that are permitted. I.e., if we have a 3x3 grid and 2 letters then clue letters can have a maximum
            // of one blank between it and the clue.

            try {

                // Parse formula string to CNF formula/clause and add to clauses.
                // Performance can be improved by building the formula as an object and not parsing as a string.
                Formula constraint = propositionalParser.parse(formula.toString());
                constraint = constraint.cnf();

                if (constraint.isAtomicFormula()) {
                    clauses.add(constraint);
                    addedClauses.add(constraint);
                } else {
                    for (Formula clause : constraint) { // Add all clauses in the CNF formula to the list of clauses.
                        clauses.add(clause);
                        addedClauses.add(clause);
                    }
                }

            } catch (ParserException e) {
                System.out.println("Error - Failed to parse: " + formula.toString());
                System.exit(-1);
            }

        }

        return addedClauses; // For testing purposes only.

    } // cluesAdhered().

    /**
     * Uses a MiniSAT solver to solve the problem.
     *
     * @return True if there is a solution, false otherwise (which means no solution or unknown).
     */
    public boolean solvePuzzle() {

        // Turn set of clauses into a formula.
        Formula formula = formulaFactory.and(clauses);

        // Attempt to solve the formula using MiniSAT.
        solver.add(formula);
        result = solver.sat();

        return (result == Tristate.TRUE); // False indicates no solution or solution unknown.

    } // solvePuzzle().

    /**
     * When the puzzle has been solved, return a Grid object with the solution in it.
     *
     * @return Grid object representing the solution.
     */
    public Grid modelToGrid() {

        Grid solutionGrid = new Grid(grid);
        Assignment model = solver.model(); // There is a solution, so we can get the model.

        for (Literal currVariable : model.positiveVariables()) {

            String currVariableName = currVariable.name();

            // Assign squares in the grid to their assigned letters in the model.
            int rowIndex = Integer.parseInt(currVariableName.substring(0, 1));
            int colIndex = Integer.parseInt(currVariableName.substring(1, 2));
            char assignedSymbol = currVariableName.substring(2, 3).charAt(0);

            solutionGrid.chars[rowIndex][colIndex] = assignedSymbol;

        }

        return solutionGrid;

    } // modelToGrid().


} // DeclarativeABC{}.
