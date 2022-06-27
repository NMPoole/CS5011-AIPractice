package tests;

import mains.Puzzle;
import mains.Grid;
import tests.SamplePuzzles;
import tests.SampleGrids;
import part1.CheckerABC;
import part2.ProceduralABC;
import part3.DeclarativeABC;

import tests.TestsBase;

/**
 * Class for running the staff tests.
 *
 * Please do not add tests to this file.
 * Feel free to add tests to the TestsStudent file.
 *
 * @author Ian Gent
 */
public class TestsStaff {

    /**
     * Run staff tests for the Part 1 code.
     */
    public static void part1() {

        Puzzle puzzle;
        Grid grid;
        Grid result;

        TestsBase.testIsConsistent("Part 1-1", true, "ABC1", "ABC1Sol");
        TestsBase.testIsFullGrid("Part 1-2", true, "ABC1", "ABC1Sol");
        TestsBase.testIsSolution("Part 1-3", true, "ABC1", "ABC1Sol");

        TestsBase.testIsConsistent("Part 1-4", true, "ABC1", "ABC1incomplete");
        TestsBase.testIsFullGrid("Part 1-5", false, "ABC1", "ABC1incomplete");
        TestsBase.testIsSolution("Part 1-6", false, "ABC1", "ABC1incomplete");

        TestsBase.testIsConsistent("Part 1-7", false, "ABC1", "ABC1inconsistent1");
        TestsBase.testIsConsistent("Part 1-8", false, "ABC1", "ABC1inconsistent2");
        TestsBase.testIsConsistent("Part 1-9", false, "ABC1", "ABC1inconsistent3");
        TestsBase.testIsConsistent("Part 1-10", false, "ABC1", "ABC1inconsistent4");
        TestsBase.testIsConsistent("Part 1-11", false, "ABC1", "ABC1inconsistent5");
        TestsBase.testIsConsistent("Part 1-12", false, "ABC1", "ABC1inconsistent6");
        TestsBase.testIsConsistent("Part 1-13", false, "ABC1", "ABC1inconsistent7");
        TestsBase.testIsConsistent("Part 1-14", false, "ABC1", "ABC1inconsistent8");
        TestsBase.testIsConsistent("Part 1-15", false, "ABC1", "ABC1inconsistent9");
        TestsBase.testIsConsistent("Part 1-16", false, "ABC1", "ABC1inconsistent10");
        TestsBase.testIsConsistent("Part 1-17", false, "ABC1", "ABC1inconsistent11");
        TestsBase.testIsConsistent("Part 1-18", false, "ABC1", "ABC1inconsistent12");

    } // part1().

    /**
     * Run staff tests for the Part 2 code.
     */
    public static void part2() {

        TestsBase.testDifferentCorners("Part 2-1", "ABC1Empty", "ABC1", "ABC1Empty");
        TestsBase.testDifferentCorners("Part 2-2", "ABCdifferentcorners6", "ABC6", "ABC6Empty");
        TestsBase.testCommonClues("Part 2-3", "ABCcommonclues1", "ABC1", "ABC6Empty");
        TestsBase.testCommonClues("Part 2-4", "ABCcommonclues6", "ABC6", "ABC6Empty");
        TestsBase.testFillInBlanksCol("Part 2-5", "ABC1testblankscolscols", "ABC1", "ABC1testcols");
        TestsBase.testFillInBlanksCol("Part 2-6", "ABC1testrowscols", "ABC1", "ABC1testrows");
        TestsBase.testFillInBlanksCol("Part 2-7", "ABC1Sol", "ABC1", "ABC1testblanks");
        TestsBase.testFillInBlanksRow("Part 2-8", "ABC1testcols", "ABC1", "ABC1testcols");
        TestsBase.testFillInBlanksRow("Part 2-9", "ABC1testblanksrowsrows", "ABC1", "ABC1testrows");
        TestsBase.testFillInBlanksRow("Part 2-10", "ABC1Sol", "ABC1", "ABC1testblanks");

        TestsBase.testOnlyPlaceRow("Part 2-11", "ABC1Sol", "ABC1", "ABC1testboth");
        TestsBase.testOnlyPlaceRow("Part 2-12", "ABC1testonlyplacerowrows", "ABC1", "ABC1testrows");
        TestsBase.testOnlyPlaceRow("Part 2-13", "ABC1Sol", "ABC1", "ABC1incomplete");
        TestsBase.testOnlyPlaceRow("Part 2-14", "ABC1testonlyplacerowcols", "ABC1", "ABC1testcols");
        TestsBase.testOnlyPlaceCol("Part 2-15", "ABC1Sol", "ABC1", "ABC1testboth");
        TestsBase.testOnlyPlaceCol("Part 2-16", "ABC1testonlyplacecolrows", "ABC1", "ABC1testrows");
        TestsBase.testOnlyPlaceCol("Part 2-17", "ABC1Sol", "ABC1", "ABC1incomplete");
        TestsBase.testOnlyPlaceCol("Part 2-18", "ABC1testonlyplacecolcols", "ABC1", "ABC1testcols");

    } // part2().

    /**
     * Run staff tests for the Part 3 code.
     */
    public static void part3() {

        TestsBase.testDeclarativeBoolean("Part 3-1-Trivial-1", true, "Trivial");
        TestsBase.testDeclarativeIsSolution("Part 3-2-Trivial-2", "Trivial");
        TestsBase.testDeclarativeModel("Part 3-3-Trivial-3", "TrivialSol", "Trivial");

        TestsBase.testDeclarativeBoolean("Part 3-4-VeryEasy-1", true, "VeryEasy");
        TestsBase.testDeclarativeIsSolution("Part 3-5-VeryEasy-2", "VeryEasy");
        TestsBase.testDeclarativeModel("Part 3-6-VeryEasy-3", "EasySol", "VeryEasy");

        TestsBase.testDeclarativeBoolean("Part 3-7-StillEasy-1", true, "StillEasy");
        TestsBase.testDeclarativeIsSolution("Part 3-8-StillEasy-2", "StillEasy");
        TestsBase.testDeclarativeModel("Part 3-9-StillEasy-3", "EasySol", "StillEasy");

        TestsBase.testDeclarativeBoolean("Part 3-10-NotHard-1", true, "NotHard");
        TestsBase.testDeclarativeIsSolution("Part 3-11-NotHard-2", "NotHard");
        TestsBase.testDeclarativeModel("Part 3-12-NotHard-3", "NotHardSol", "NotHard");

        TestsBase.testDeclarativeBoolean("Part 3-13-FourByFour-1", true, "FourByFour");
        TestsBase.testDeclarativeIsSolution("Part 3-14-FourByFour-2", "FourByFour");
        TestsBase.testDeclarativeModel("Part 3-15-FourByFour-3", "FourByFourSol", "FourByFour");

        TestsBase.testDeclarativeBoolean("Part 3-16-FiveByFive-1", true, "FiveByFive");
        TestsBase.testDeclarativeIsSolution("Part 3-17-FiveByFive-2", "FiveByFive");
        TestsBase.testDeclarativeModel("Part 3-18-FiveByFive-3", "FiveByFiveSol", "FiveByFive");

        TestsBase.testDeclarativeBoolean("Part 3-19-ABC1-1", true, "ABC1");
        TestsBase.testDeclarativeIsSolution("Part 3-20-ABC1-2", "ABC1");
        TestsBase.testDeclarativeModel("Part 3-21-ABC1-3", "ABC1Sol", "ABC1");

        TestsBase.testDeclarativeBoolean("Part 3-22-ABC6-1", true, "ABC6");
        TestsBase.testDeclarativeIsSolution("Part 3-23-ABC6-2", "ABC6");
        TestsBase.testDeclarativeModel("Part 3-24-ABC6-3", "ABC6Sol", "ABC6");

        TestsBase.testDeclarativeBoolean("Part 3-25-SevenBySeven-1", true, "SevenBySeven");
        TestsBase.testDeclarativeIsSolution("Part 3-26-SevenBySeven-2", "SevenBySeven");

        TestsBase.testDeclarativeBoolean("Part 3-27-AUGUST-1", true, "AUGUST");
        TestsBase.testDeclarativeIsSolution("Part 3-28-AUGUST-2", "AUGUST");


        TestsBase.testDeclarativeBoolean("Part 3-29-ABC1unsolvable1", false, "ABCunsolvable1");
        TestsBase.testDeclarativeBoolean("Part 3-30-ABC1unsolvable2", false, "ABCunsolvable2");

    } // part3().


} // TestsStaff{}.


