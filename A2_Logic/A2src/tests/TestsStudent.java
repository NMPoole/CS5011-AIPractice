package tests;

import mains.Grid;
import mains.Puzzle;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import part2.ProceduralABC;
import part3.DeclarativeABC;
import part4.Hint;
import part4.HintABC;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class for running the student tests.
 *
 * @author 170004680
 * @author Alice Toniolo, Ian Gent.
 */
public class TestsStudent {


    /**
     * Additional tests for Part 1.
     */
    public static void part1() {

        // Run all remaining tests that can be carried out for the provided test instances.
        part1MissingStaffTests();

    } // part1().

    /**
     * Run tests for Part 1 that can be carried out using the provided instances but aren't included in TestsStaff.
     */
    public static void part1MissingStaffTests() {

        // Tests which are solutions:

        TestsBase.testIsFullGrid("Part 1-1", true, "Trivial", "TrivialSol");
        TestsBase.testIsConsistent("Part 1-1", true, "Trivial", "TrivialSol");
        TestsBase.testIsSolution("Part 1-1", true, "Trivial", "TrivialSol");

        TestsBase.testIsFullGrid("Part 1-2", true, "VeryEasy", "EasySol");
        TestsBase.testIsConsistent("Part 1-2", true, "VeryEasy", "EasySol");
        TestsBase.testIsSolution("Part 1-2", true, "VeryEasy", "EasySol");

        TestsBase.testIsFullGrid("Part 1-3", true, "StillEasy", "EasySol");
        TestsBase.testIsConsistent("Part 1-3", true, "StillEasy", "EasySol");
        TestsBase.testIsSolution("Part 1-3", true, "StillEasy", "EasySol");

        TestsBase.testIsFullGrid("Part 1-4", true, "NotHard", "NotHardSol");
        TestsBase.testIsConsistent("Part 1-4", true, "NotHard", "NotHardSol");
        TestsBase.testIsSolution("Part 1-4", true, "NotHard", "NotHardSol");

        TestsBase.testIsFullGrid("Part 1-5", true, "FourByFour", "FourByFourSol");
        TestsBase.testIsConsistent("Part 1-5", true, "FourByFour", "FourByFourSol");
        TestsBase.testIsSolution("Part 1-5", true, "FourByFour", "FourByFourSol");

        TestsBase.testIsFullGrid("Part 1-6", true, "FiveByFive", "FiveByFiveSol");
        TestsBase.testIsConsistent("Part 1-6", true, "FiveByFive", "FiveByFiveSol");
        TestsBase.testIsSolution("Part 1-6", true, "FiveByFive", "FiveByFiveSol");

        TestsBase.testIsFullGrid("Part 1-7", true, "SevenBySeven", "SevenBySevenSol");
        TestsBase.testIsConsistent("Part 1-7", true, "SevenBySeven", "SevenBySevenSol");
        TestsBase.testIsSolution("Part 1-7", true, "SevenBySeven", "SevenBySevenSol");

        TestsBase.testIsFullGrid("Part 1-8", true, "ABC6", "ABC6Sol");
        TestsBase.testIsConsistent("Part 1-8", true, "ABC6", "ABC6Sol");
        TestsBase.testIsSolution("Part 1-8", true, "ABC6", "ABC6Sol");

        // Already in staff tests:
        // TestsBase.testIsFullGrid("Part 1-9", true, "ABC1", "ABC1Sol");
        // TestsBase.testIsConsistent("Part 1-9", true, "ABC1", "ABC1Sol");
        // TestsBase.testIsSolution("Part 1-9", true, "ABC1", "ABC1Sol");


        // Tests which are consistent and not full, or not consistent and full:

        TestsBase.testIsFullGrid("Part 1-10", false, "ABC6", "ABC6Empty");
        TestsBase.testIsConsistent("Part 1-10", true, "ABC6", "ABC6Empty"); // Consistent.
        TestsBase.testIsSolution("Part 1-10", false, "ABC6", "ABC6Empty");

        TestsBase.testIsFullGrid("Part 1-11", false, "ABC1", "ABC1Empty");
        TestsBase.testIsConsistent("Part 1-12", true, "ABC1", "ABC1Empty"); // Consistent.
        TestsBase.testIsSolution("Part 1-12", false, "ABC1", "ABC1Empty");

        // Already in staff tests:
        // TestsBase.testIsFullGrid("Part 1-13", false, "ABC1", "ABC1incomplete");
        // TestsBase.testIsConsistent("Part 1-13", true, "ABC1", "ABC1incomplete"); // Consistent.
        // TestsBase.testIsSolution("Part 1-13", false, "ABC1", "ABC1incomplete");

        TestsBase.testIsFullGrid("Part 1-14", false, "ABC1", "ABC1inconsistent4");
        TestsBase.testIsFullGrid("Part 1-14", false, "ABC1", "ABC1inconsistent5");
        TestsBase.testIsFullGrid("Part 1-14", false, "ABC1", "ABC1inconsistent6");
        TestsBase.testIsFullGrid("Part 1-14", false, "ABC1", "ABC1inconsistent7");
        TestsBase.testIsFullGrid("Part 1-14", false, "ABC1", "ABC1inconsistent8");
        TestsBase.testIsFullGrid("Part 1-14", false, "ABC1", "ABC1inconsistent9");
        TestsBase.testIsFullGrid("Part 1-14", false, "ABC1", "ABC1inconsistent10");
        TestsBase.testIsFullGrid("Part 1-14", false, "ABC1", "ABC1inconsistent11");
        TestsBase.testIsFullGrid("Part 1-14", false, "ABC1", "ABC1inconsistent12");

        TestsBase.testIsFullGrid("Part 1-15", false, "ABC1", "ABC1testcols");
        TestsBase.testIsConsistent("Part 1-15", true, "ABC1", "ABC1testcols"); // Consistent.
        TestsBase.testIsSolution("Part 1-15", false, "ABC1", "ABC1testcols");

        TestsBase.testIsFullGrid("Part 1-16", false, "ABC1", "ABC1testblankscolscols");
        TestsBase.testIsConsistent("Part 1-16", true, "ABC1", "ABC1testblankscolscols"); // Consistent.
        TestsBase.testIsSolution("Part 1-16", false, "ABC1", "ABC1testblankscolscols");

        TestsBase.testIsFullGrid("Part 1-17", false, "ABC1", "ABC1testblanksrowsrows");
        TestsBase.testIsConsistent("Part 1-17", true, "ABC1", "ABC1testblanksrowsrows"); // Consistent.
        TestsBase.testIsSolution("Part 1-17", false, "ABC1", "ABC1testblanksrowsrows");

        TestsBase.testIsFullGrid("Part 1-18", false, "ABC1", "ABC1testrows");
        TestsBase.testIsConsistent("Part 1-18", true, "ABC1", "ABC1testrows"); // Consistent.
        TestsBase.testIsSolution("Part 1-18", false, "ABC1", "ABC1testrows");

        TestsBase.testIsFullGrid("Part 1-19", false, "ABC1", "ABC1testrowscols");
        TestsBase.testIsConsistent("Part 1-19", true, "ABC1", "ABC1testrowscols"); // Consistent.
        TestsBase.testIsSolution("Part 1-19", false, "ABC1", "ABC1testrowscols");

        TestsBase.testIsFullGrid("Part 1-20", false, "ABC1", "ABC1testboth");
        TestsBase.testIsConsistent("Part 1-20", true, "ABC1", "ABC1testboth"); // Consistent.
        TestsBase.testIsSolution("Part 1-20", false, "ABC1", "ABC1testboth");

        TestsBase.testIsFullGrid("Part 1-21", false, "ABC1", "ABCdifferentcorners6");
        TestsBase.testIsConsistent("Part 1-21", true, "ABC1", "ABCdifferentcorners6"); // Consistent.
        TestsBase.testIsSolution("Part 1-21", false, "ABC1", "ABCdifferentcorners6");

        TestsBase.testIsFullGrid("Part 1-22", false, "ABC1", "ABCcommonclues1");
        TestsBase.testIsConsistent("Part 1-22", true, "ABC1", "ABCcommonclues1"); // Consistent.
        TestsBase.testIsSolution("Part 1-22", false, "ABC1", "ABCcommonclues1");

        TestsBase.testIsFullGrid("Part 1-24", false, "ABC1", "ABC1testrowsrows");
        TestsBase.testIsConsistent("Part 1-24", true, "ABC1", "ABC1testrowsrows"); // Consistent.
        TestsBase.testIsSolution("Part 1-24", false, "ABC1", "ABC1testrowsrows");

        TestsBase.testIsFullGrid("Part 1-25", false, "ABC1", "ABC1testcolsrows");
        TestsBase.testIsConsistent("Part 1-25", true, "ABC1", "ABC1testcolsrows"); // Consistent.
        TestsBase.testIsSolution("Part 1-25", false, "ABC1", "ABC1testcolsrows");

        TestsBase.testIsFullGrid("Part 1-26", false, "ABC1", "ABC1testonlyplacecolrows");
        TestsBase.testIsConsistent("Part 1-26", true, "ABC1", "ABC1testonlyplacecolrows"); // Consistent.
        TestsBase.testIsSolution("Part 1-26", false, "ABC1", "ABC1testonlyplacecolrows");

        TestsBase.testIsFullGrid("Part 1-27", false, "ABC1", "ABC1testonlyplacecolcols");
        TestsBase.testIsConsistent("Part 1-27", true, "ABC1", "ABC1testonlyplacecolcols"); // Consistent.
        TestsBase.testIsSolution("Part 1-27", false, "ABC1", "ABC1testonlyplacecolcols");

        TestsBase.testIsFullGrid("Part 1-28", false, "ABC1", "ABC1testonlyplacerowrows");
        TestsBase.testIsConsistent("Part 1-28", true, "ABC1", "ABC1testonlyplacerowrows"); // Consistent.
        TestsBase.testIsSolution("Part 1-28", false, "ABC1", "ABC1testonlyplacerowrows");

        TestsBase.testIsFullGrid("Part 1-29", false, "ABC1", "ABC1testonlyplacerowcols");
        TestsBase.testIsConsistent("Part 1-29", true, "ABC1", "ABC1testonlyplacerowcols"); // Consistent.
        TestsBase.testIsSolution("Part 1-29", false, "ABC1", "ABC1testonlyplacerowcols");

        TestsBase.testIsFullGrid("Part 1-30", false, "ABC1", "ABC1testblanks");
        TestsBase.testIsConsistent("Part 1-30", true, "ABC1", "ABC1testblanks"); // Consistent.
        TestsBase.testIsSolution("Part 1-30", false, "ABC1", "ABC1testblanks");


        // Neither consistent nor full test:

        TestsBase.testIsFullGrid("Part 1-23", false, "ABC1", "ABCcommonclues6");
        TestsBase.testIsConsistent("Part 1-23", false, "ABC1", "ABCcommonclues6");
        TestsBase.testIsSolution("Part 1-23", false, "ABC1", "ABCcommonclues6");

    } // part1MissingStaffTests()

    /**
     * Additional tests for Part 2.
     */
    public static void part2() {

        // onlyPlaceForLetterRow() Tests:

        // Tests: If one remaining square and one remaining letter in a row, then fill.
        TestsBase.testOnlyPlaceRow("Part2-1", "Student2Sol", "StudentSmall2", "Student2TopLeft");
        TestsBase.testOnlyPlaceRow("Part2-1", "Student2Sol", "StudentSmall2", "Student2TopRight");
        TestsBase.testOnlyPlaceRow("Part2-1", "Student2Sol", "StudentSmall2", "Student2BottomLeft");
        TestsBase.testOnlyPlaceRow("Part2-1", "Student2Sol", "StudentSmall2", "Student2BottomRight");
        // Tests: If multiple remaining squares and one remaining letter, then can't fill.
        TestsBase.testOnlyPlaceRow("Part2-2", "Student3OnlyAs", "StudentSmall3", "Student3OnlyAs");
        // Tests: If one remaining square, but multiple remaining letters, then can't fill.
        TestsBase.testOnlyPlaceRow("Part2-3", "Student3MultipleLettersRemain", "StudentSmall3", "Student3MultipleLettersRemain");
        // Tests: If >1 remaining square and >1 remaining letter, then can't fill.
        TestsBase.testOnlyPlaceRow("Part2-4", "Student3MultipleLettersSquaresRemain", "StudentSmall3", "Student3MultipleLettersSquaresRemain");

        // onlyPlaceForLetterCol() Tests:

        // Tests: If one remaining square and one remaining letter in a column, then fill.
        TestsBase.testOnlyPlaceCol("Part2-5", "Student2Sol", "StudentSmall2", "Student2TopLeft");
        TestsBase.testOnlyPlaceCol("Part2-5", "Student2Sol", "StudentSmall2", "Student2TopRight");
        TestsBase.testOnlyPlaceCol("Part2-5", "Student2Sol", "StudentSmall2", "Student2BottomLeft");
        TestsBase.testOnlyPlaceCol("Part2-5", "Student2Sol", "StudentSmall2", "Student2BottomRight");
        // Tests: If multiple remaining squares and one remaining letter, then can't fill.
        TestsBase.testOnlyPlaceCol("Part2-6", "Student3OnlyAs", "StudentSmall3", "Student3OnlyAs");
        // Tests: If one remaining square, but multiple remaining letters, then can't fill.
        TestsBase.testOnlyPlaceCol("Part2-7", "Student3MultipleLettersRemain", "StudentSmall3", "Student3MultipleLettersRemain");
        // Tests: If >1 remaining square and >1 remaining letter, then can't fill.
        TestsBase.testOnlyPlaceRow("Part2-8", "Student3MultipleLettersSquaresRemain", "StudentSmall3", "Student3MultipleLettersSquaresRemain");

        // fillInBlanksRow() Tests:

        // Tests: If all letters in row, then remaining unfilled squares are filled in as blanks [1 unfilled square].
        TestsBase.testFillInBlanksRow("Part2-9", "Student4BlanksFilledRow", "StudentSmall4", "Student4BlanksFill");
        // Tests: If all letters in row, then remaining unfilled squares are filled in as blanks [>1 unfilled square].
        TestsBase.testFillInBlanksRow("Part2-10", "Student4_2ExpectedRow", "StudentSmall4_2", "Student4_2");
        // Tests: If not all letters in a row, then do not fill in unfilled squares with blanks.
        TestsBase.testFillInBlanksRow("Part2-11", "Student4_2", "StudentSmall4", "Student4_2");

        // fillInBlanksCol() Tests:

        // Tests: If all letters in col, then remaining unfilled squares are filled in as blanks.
        TestsBase.testFillInBlanksCol("Part2-12", "Student4BlanksFilledCol", "StudentSmall4", "Student4BlanksFill");
        // Tests: If all letters in col, then remaining unfilled squares are filled in as blanks [>1 unfilled square].
        TestsBase.testFillInBlanksCol("Part2-13", "Student4_2ExpectedCol", "StudentSmall4_2", "Student4_2");
        // Tests: If not all letters in a col, then do not fill in unfilled squares with blanks.
        TestsBase.testFillInBlanksCol("Part2-14", "Student4_2", "StudentSmall4", "Student4_2");

        // differentCorners() Tests:

        // Tests: If in one corner the adjacent clues differ, then fill with blank.
        TestsBase.testDifferentCorners("Part2-15", "Student5Expected", "StudentSmall5", "Student5");
        // Tests: If in multiple corners the adjacent clues differ, then fill all with blanks.
        TestsBase.testDifferentCorners("Part2-16", "Student5_2Expected", "StudentSmall5_2", "Student5");
        // Tests: Do not fill corners if adjacent clues match.
        TestsBase.testDifferentCorners("Part2-17", "Student5", "StudentSmall5_3", "Student5");

        // commonClues() Tests:

        // Tests: Fill in squares where common clues occur [1 square].
        TestsBase.testCommonClues("Part2-18", "Student2Sol", "StudentSmall2", "Student2TopLeft");
        // Tests: Fill in squares where common clues occur [>1 square].
        TestsBase.testCommonClues("Part2-19", "Student2Sol", "StudentSmall2", "Student2Empty");
        // Tests: Do not fill in squares where no common clues.
        TestsBase.testCommonClues("Part2-20", "Student5", "StudentSmall5_3", "Student5");
        // Tests: Do not fill in squares if unfilled spots in clues exist.
        TestsBase.testCommonClues("Part2-21", "Student6", "StudentSmall6", "Student6");

    } // part2().

    /**
     * Additional tests for Part 3.
     */
    public static void part3() {

        FormulaFactory formulaFactory = new FormulaFactory();
        PropositionalParser propositionalParser = new PropositionalParser(formulaFactory);

        // Create propositional variables for a given square correctly.
        DeclarativeABC decABC = new DeclarativeABC();
        decABC.setup(SamplePuzzles.valueOf("Trivial").puzzle, SampleGrids.valueOf("TrivialSol").grid);
        HashSet<String> variables = decABC.createPropositionalVariables();
        HashSet<String> expected = new HashSet<String>(){{ add("00x"); add("00a"); }};
        TestsBase.printTestResult(expected.equals(variables), "createPropositionalVariables", "Part3-1");

        // Constraints for enforcing every square has at least one symbol done correctly.
        HashSet<Formula> clauses = decABC.everySquareAtLeastOneSymbol();
        String clause = clauses.stream().findFirst().toString();
        TestsBase.printTestResult(clause.contains("00a | 00x"), "everySquareAtLeastOneSymbol", "Part3-2");

        // Constraints for enforcing every square has at most one symbol done correctly.
        clauses = decABC.everySquareMaxOneSymbol();
        clause = clauses.stream().findFirst().toString();
        TestsBase.printTestResult(clause.contains("~00a | ~00x"), "everySquareAtMostOneSymbol", "Part3-3");

        // Constraints for enforcing every row has every letter at least once is correct.
        clauses = decABC.rowEveryLetterAtLeastOnce();
        clause = clauses.stream().findFirst().toString();
        TestsBase.printTestResult(clause.contains("00a"), "rowEveryLetterAtLeastOnce", "Part3-4");

        // Constraints for enforcing every column has every letter at least once is correct.
        clauses = decABC.colEveryLetterAtLeastOnce();
        clause = clauses.stream().findFirst().toString();
        TestsBase.printTestResult(clause.contains("00a"), "colEveryLetterAtLeastOnce", "Part3-5");

        // Constraints for enforcing every row has every letter at most once is correct.
        clauses = decABC.rowEveryLetterAtMostOnce();
        clause = clauses.stream().findFirst().toString();
        TestsBase.printTestResult(clause.contains("empty"), "rowEveryLetterAtMostOnce", "Part3-6");

        // Constraints for enforcing every row has every letter at most once is correct.
        clauses = decABC.colEveryLetterAtMostOnce();
        clause = clauses.stream().findFirst().toString();
        TestsBase.printTestResult(clause.contains("empty"), "colEveryLetterAtMostOnce", "Part3-7");

        // Constraints for enforcing all clues to be adhered for all rows are correct.
        clauses = decABC.cluesAdheredForRows();
        clause = clauses.stream().findFirst().toString();
        TestsBase.printTestResult(clause.contains("00a"), "cluesAdheredForRows", "Part3-8");

        // Constraints for enforcing all clues to be adhered for all columns are correct.
        clauses = decABC.cluesAdheredForCols();
        clause = clauses.stream().findFirst().toString();
        TestsBase.printTestResult(clause.contains("00a"), "cluesAdheredForCols", "Part3-9");

        // Create propositional variables for all squares correctly.
        decABC = new DeclarativeABC();
        decABC.setup(SamplePuzzles.valueOf("VeryEasy").puzzle, SampleGrids.valueOf("EasySol").grid);
        variables = decABC.createPropositionalVariables();
        expected = new HashSet<String>(){{ add("00a"); add("00b"); add("00x");
                                    add("01a"); add("01b"); add("01x");
                                    add("10a"); add("10b"); add("10x");
                                    add("11a");add("11b");add("11x");}};
        TestsBase.printTestResult(expected.equals(variables), "createPropositionalVariables", "Part3-10");

        try {
            // Constraints for enforcing every square has at least one symbol done correctly.
            clauses = decABC.everySquareAtLeastOneSymbol();
            TestsBase.printTestResult(clauses.contains(propositionalParser.parse("00a | 00b | 00x"))
                            && clauses.contains(propositionalParser.parse("01a | 01b | 01x"))
                            && clauses.contains(propositionalParser.parse("10a | 10b | 10x"))
                            && clauses.contains(propositionalParser.parse("11a | 11b | 11x")),
                    "everySquareAtLeastOneSymbol", "Part3-11");
        } catch (ParserException ignored) {
            // Ignore.
        }

        try {
            // Constraints for enforcing every square has at most one symbol done correctly.
            clauses = decABC.everySquareMaxOneSymbol();
            TestsBase.printTestResult(clauses.contains(propositionalParser.parse("~00a | ~00b"))
                                        && clauses.contains(propositionalParser.parse("~00a | ~00x"))
                                        && clauses.contains(propositionalParser.parse("~00b | ~00x"))
                                        && clauses.contains(propositionalParser.parse("~01a | ~01b"))
                                        && clauses.contains(propositionalParser.parse("~01a | ~01x"))
                                        && clauses.contains(propositionalParser.parse("~01b | ~01x"))
                                        && clauses.contains(propositionalParser.parse("~10a | ~10b"))
                                        && clauses.contains(propositionalParser.parse("~10a | ~10x"))
                                        && clauses.contains(propositionalParser.parse("~10b | ~10x"))
                                        && clauses.contains(propositionalParser.parse("~11a | ~11b"))
                                        && clauses.contains(propositionalParser.parse("~11a | ~11x"))
                                        && clauses.contains(propositionalParser.parse("~11b | ~11x")),
                    "everySquareMaxOneSymbol", "Part3-12");
        } catch (ParserException ignored) {
            // Ignore.
        }

        try {
            // Constraints for enforcing every row has at least one letter done correctly.
            clauses = decABC.rowEveryLetterAtLeastOnce();
            TestsBase.printTestResult(clauses.contains(propositionalParser.parse("00a | 01a"))
                            && clauses.contains(propositionalParser.parse("00b | 01b"))
                            && clauses.contains(propositionalParser.parse("10a | 11a"))
                            && clauses.contains(propositionalParser.parse("10b | 11b")),
                    "rowEveryLetterAtLeastOnce", "Part3-13");
        } catch (ParserException ignored) {
            // Ignore.
        }

        try {
            // Constraints for enforcing every column has at least one letter done correctly.
            clauses = decABC.colEveryLetterAtLeastOnce();
            TestsBase.printTestResult(clauses.contains(propositionalParser.parse("00a | 10a"))
                            && clauses.contains(propositionalParser.parse("00b | 10b"))
                            && clauses.contains(propositionalParser.parse("01a | 11a"))
                            && clauses.contains(propositionalParser.parse("01b | 11b")),
                    "colEveryLetterAtLeastOnce", "Part3-14");
        } catch (ParserException ignored) {
            // Ignore.
        }

        try {
            // Constraints for enforcing every row has at most one letter done correctly.
            clauses = decABC.rowEveryLetterAtMostOnce();
            TestsBase.printTestResult(clauses.contains(propositionalParser.parse("~00a | ~01a"))
                            && clauses.contains(propositionalParser.parse("~00b | ~01b"))
                            && clauses.contains(propositionalParser.parse("~10a | ~11a"))
                            && clauses.contains(propositionalParser.parse("~10b | ~11b")),
                    "rowEveryLetterAtMostOnce", "Part3-15");
        } catch (ParserException ignored) {
            // Ignore.
        }

        try {
            // Constraints for enforcing every column has at most one letter done correctly.
            clauses = decABC.colEveryLetterAtMostOnce();
            TestsBase.printTestResult(clauses.contains(propositionalParser.parse("~00a | ~10a"))
                            && clauses.contains(propositionalParser.parse("~00b | ~10b"))
                            && clauses.contains(propositionalParser.parse("~01a | ~11a"))
                            && clauses.contains(propositionalParser.parse("~01b | ~11b")),
                    "colEveryLetterAtMostOnce", "Part3-16");
        } catch (ParserException ignored) {
            // Ignore.
        }

        // Constraints for enforcing all clues to be adhered for all rows are correct.
        clauses = decABC.cluesAdheredForRows();
        TestsBase.printTestResult(clauses.contains(formulaFactory.variable("00a"))
                && clauses.contains(formulaFactory.variable("01b"))
                && clauses.contains(formulaFactory.variable("10b"))
                && clauses.contains(formulaFactory.variable("11a")), "cluesAdheredForRows", "Part3-17");

        // Constraints for enforcing all clues to be adhered for all columns are correct.
        clauses = decABC.cluesAdheredForCols();
        TestsBase.printTestResult(clauses.contains(formulaFactory.variable("00a"))
                && clauses.contains(formulaFactory.variable("01b"))
                && clauses.contains(formulaFactory.variable("10b"))
                && clauses.contains(formulaFactory.variable("11a")), "cluesAdheredForCols", "Part3-18");


    } // part3().

    /**
     * Tests for Part 4.
     */
    public static void part4() {

        ArrayList<Hint> hints;
        Puzzle p;

        // Test that onlyPlaceForLetterCol returns zero hints when it cannot be applied.
        p = SamplePuzzles.valueOf("StudentSmall2").puzzle;
        hints = ProceduralABC.onlyPlaceForLetterColHints(p, new Grid(p.size()));
        TestsBase.printTestResult(hints.size() == 0, "onlyPlaceForLetterColHints", "Part4-1");

        // Test that onlyPlaceForLetterCol returns correct number of hints when it can be applied.
        p = SamplePuzzles.valueOf("StudentSmall2").puzzle;
        hints = ProceduralABC.onlyPlaceForLetterColHints(p, new Grid(SampleGrids.valueOf("Student2TopLeft").grid));
        TestsBase.printTestResult(hints.size() == 1, "onlyPlaceForLetterColHints", "Part4-2");

        // Test that onlyPlaceForLetterRow returns zero hints when it cannot be applied.
        p = SamplePuzzles.valueOf("StudentSmall2").puzzle;
        hints = ProceduralABC.onlyPlaceForLetterRowHints(p, new Grid(p.size()));
        TestsBase.printTestResult(hints.size() == 0, "onlyPlaceForLetterRowHints", "Part4-3");

        // Test that onlyPlaceForLetterRow returns correct number of hints when it can be applied.
        p = SamplePuzzles.valueOf("StudentSmall2").puzzle;
        hints = ProceduralABC.onlyPlaceForLetterRowHints(p, new Grid(SampleGrids.valueOf("Student2TopLeft").grid));
        TestsBase.printTestResult(hints.size() == 1, "onlyPlaceForLetterRowHints", "Part4-4");

        // Test that fillInBlanksCol returns zero hints when it cannot be applied.
        p = SamplePuzzles.valueOf("StudentSmall2").puzzle;
        hints = ProceduralABC.fillInBlanksColHints(p, new Grid(SampleGrids.valueOf("Student2TopLeft").grid));
        TestsBase.printTestResult(hints.size() == 0, "fillInBlanksColHints", "Part4-5");

        // Test that fillInBlanksCol returns correct number of hints when it can be applied.
        p = SamplePuzzles.valueOf("StudentSmall4").puzzle;
        hints = ProceduralABC.fillInBlanksColHints(p, new Grid(SampleGrids.valueOf("Student4BlanksFill").grid));
        TestsBase.printTestResult(hints.size() == 3, "fillInBlanksColHints", "Part4-6");

        // Test that fillInBlanksRow returns zero hints when it cannot be applied.
        p = SamplePuzzles.valueOf("StudentSmall2").puzzle;
        hints = ProceduralABC.fillInBlanksRowHints(p, new Grid(SampleGrids.valueOf("Student2TopLeft").grid));
        TestsBase.printTestResult(hints.size() == 0, "fillInBlanksRowHints", "Part4-7");

        // Test that fillInBlanksRow returns correct number of hints when it can be applied.
        p = SamplePuzzles.valueOf("StudentSmall4").puzzle;
        hints = ProceduralABC.fillInBlanksRowHints(p, new Grid(SampleGrids.valueOf("Student4BlanksFill").grid));
        TestsBase.printTestResult(hints.size() == 3, "fillInBlanksRowHints", "Part4-6");

        // Test that differentCorners returns zero hints when it cannot be applied.
        p = SamplePuzzles.valueOf("StudentSmall2").puzzle;
        hints = ProceduralABC.differentCornersHints(p, new Grid(p.size()));
        TestsBase.printTestResult(hints.size() == 0, "differentCornersHints", "Part4-9");

        // Test that differentCorners returns correct number of hints when it can be applied.
        p = SamplePuzzles.valueOf("StudentSmall5_2").puzzle;
        hints = ProceduralABC.differentCornersHints(p, new Grid(p.size()));
        TestsBase.printTestResult(hints.size() == 4, "differentCornersHints", "Part4-10");

        // Test that commonClues returns zero hints when it cannot be applied.
        p = SamplePuzzles.valueOf("StudentSmall5_2").puzzle;
        hints = ProceduralABC.commonCluesHints(p, new Grid(p.size()));
        TestsBase.printTestResult(hints.size() == 0, "commonCluesHints", "Part4-11");

        // Test that commonClues returns correct number of hints when it can be applied.
        p = SamplePuzzles.valueOf("StudentSmall2").puzzle;
        hints = ProceduralABC.commonCluesHints(p, new Grid(p.size()));
        TestsBase.printTestResult(hints.size() == 8, "commonCluesHints", "Part4-12");

        // Test that a correct and complete set of hints are provided for a trivial case.
        p = SamplePuzzles.valueOf("Trivial").puzzle;
        hints = HintABC.giveHints(p, false);
        TestsBase.printTestResult((hints != null) && (hints.size() == 1), "giveHints", "Part4-13");

        // Test that a correct and complete set of hints are provided for an easy case.
        p = SamplePuzzles.valueOf("VeryEasy").puzzle;
        hints = HintABC.giveHints(p, false);
        TestsBase.printTestResult((hints != null) && (hints.size() == 4), "giveHints", "Part4-14");

        // Test that an appropriate message is given when the hint system cannot provide a complete set of hints.
        p = SamplePuzzles.valueOf("ABCunsolvable1").puzzle;
        hints = HintABC.giveHints(p, false);
        TestsBase.printTestResult(hints == null, "giveHintsUnsolvable", "Part4-15");

    } // part4().


} // TestsStudent{}.


