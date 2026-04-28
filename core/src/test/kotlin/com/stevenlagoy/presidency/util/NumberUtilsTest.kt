package com.stevenlagoy.presidency.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NumberUtilsTest {

    @Test
    fun `WHEN clamping integer values THEN values are clamped`() {
        assertEquals(5, clamp(5, 0, 10))
        assertEquals(10, clamp(20, 0, 10))
        assertEquals(0, clamp(-10, 0, 10))
    }

    @Test
    fun `WHEN clamping double values THEN values are clamped`() {
        assertEquals(2.5, clamp(2.5, 0.5, 5.0))
        assertEquals(5.0, clamp(5.5, 0.5, 5.0))
        assertEquals(0.5, clamp(0.0, 0.5, 5.0))
    }

    @Test
    fun `WHEN checking prime-ness THEN returned value is correct`() {
        assertTrue  { 1.isPrime() }
        assertTrue  { 2.isPrime() }
        assertTrue  { 3.isPrime() }
        assertFalse { 4.isPrime() }
        assertTrue  { 5.isPrime() }
        assertFalse { 6.isPrime() }
        assertTrue  { 7.isPrime() }
        assertFalse { 8.isPrime() }
        assertFalse { 9.isPrime() }
        assertFalse { 10.isPrime() }
        assertTrue  { 11.isPrime() }
        assertFalse { 12.isPrime() }
        assertTrue  { 13.isPrime() }
        assertFalse { 14.isPrime() }
        assertFalse { 15.isPrime() }
        assertFalse { 16.isPrime() }
        assertTrue  { 17.isPrime() }
        assertFalse { 18.isPrime() }
        assertTrue  { 19.isPrime() }
        assertFalse { 20.isPrime() }
        val primes = listOf(
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83,
            89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179,
            181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277,
            281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389,
            397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499,
            503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617,
            619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739,
            743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859,
            863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991,
            997, 1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061, 1063, 1069, 1087, 1091,
            1093, 1097, 1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163, 1171, 1181, 1187, 1193, 1201,
            1213, 1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283, 1289, 1291, 1297, 1301,
            1303, 1307, 1319, 1321, 1327, 1361, 1367, 1373, 1381, 1399, 1409, 1423, 1427, 1429, 1433,
            1439, 1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499, 1511, 1523, 1531,
            1543, 1549, 1553, 1559, 1567, 1571, 1579, 1583, 1597, 1601, 1607, 1609, 1613, 1619, 1621,
            1627, 1637, 1657, 1663, 1667, 1669, 1693, 1697, 1699, 1709, 1721, 1723, 1733, 1741, 1747,
            1753, 1759, 1777, 1783, 1787, 1789, 1801, 1811, 1823, 1831, 1847, 1861, 1867, 1871, 1873,
            1877, 1879, 1889, 1901, 1907, 1913, 1931, 1933, 1949, 1951, 1973, 1979, 1987, 1993, 1997, 1999,
        )
        for (i in 2..2000) {
            if (i in primes)
                assertTrue { i.isPrime() }
            else
                assertFalse { i.isPrime() }
        }
    }

    @Test
    fun `WHEN getting next prime THEN returned prime is correct`() {
        assertEquals(1, (-10).nextPrime())
        assertEquals(11, 10.nextPrime())
        assertEquals(17, 13.nextPrime())
        assertEquals(97, 89.nextPrime())
        assertEquals(19_661, 19_609.nextPrime())
        assertEquals(1_693_182_318_747_503L, 1_693_182_318_746_371L.nextPrime())
    }

    @Test
    fun `WHEN converting numbers to ordinal forms THEN ordinal representations are correct`() {
        assertEquals("0th",   0.toOrdinal())
        assertEquals("1st",   1.toOrdinal())
        assertEquals("2nd",   2.toOrdinal())
        assertEquals("3rd",   3.toOrdinal())
        assertEquals("4th",   4.toOrdinal())
        assertEquals("5th",   5.toOrdinal())
        assertEquals("6th",   6.toOrdinal())
        assertEquals("7th",   7.toOrdinal())
        assertEquals("8th",   8.toOrdinal())
        assertEquals("9th",   9.toOrdinal())
        assertEquals("10th",  10.toOrdinal())
        assertEquals("11th",  11.toOrdinal())
        assertEquals("12th",  12.toOrdinal())
        assertEquals("13th",  13.toOrdinal())
        assertEquals("14th",  14.toOrdinal())
        assertEquals("15th",  15.toOrdinal())
        assertEquals("16th",  16.toOrdinal())
        assertEquals("17th",  17.toOrdinal())
        assertEquals("18th",  18.toOrdinal())
        assertEquals("19th",  19.toOrdinal())
        assertEquals("20th",  20.toOrdinal())
        assertEquals("21st",  21.toOrdinal())
        assertEquals("22nd",  22.toOrdinal())
        assertEquals("23rd",  23.toOrdinal())
        assertEquals("24th",  24.toOrdinal())
        assertEquals("25th",  25.toOrdinal())
        assertEquals("26th",  26.toOrdinal())
        assertEquals("27th",  27.toOrdinal())
        assertEquals("28th",  28.toOrdinal())
        assertEquals("29th",  29.toOrdinal())
        assertEquals("30th",  30.toOrdinal())
        assertEquals("40th",  40.toOrdinal())
        assertEquals("50th",  50L.toOrdinal())
        assertEquals("60th",  60L.toOrdinal())
        assertEquals("70th",  70L.toOrdinal())
        assertEquals("80th",  80L.toOrdinal())
        assertEquals("90th",  90L.toOrdinal())
        assertEquals("100th", 100L.toOrdinal())
        assertEquals("negative 1st", (-1).toOrdinal())
        assertEquals("negative 2nd", (-2).toOrdinal())
        assertEquals("negative 3rd", (-3).toOrdinal())
        for (i in 1..100) {
            assertFalse(i.toOrdinal().isBlank())
            assertEquals("negative ${i.toOrdinal()}", (-i).toOrdinal())
        }
    }

    @Test
    fun `WHEN converting whole numbers between 0 and 10 to words THEN word representations are correct`() {
        assertEquals("zero",  0.toWords())
        assertEquals("one",   1.toWords())
        assertEquals("two",   2.toWords())
        assertEquals("three", 3.toWords())
        assertEquals("four",  4.toWords())
        assertEquals("five",  5.toWords())
        assertEquals("six",   6.toWords())
        assertEquals("seven", 7.toWords())
        assertEquals("eight", 8.toWords())
        assertEquals("nine",  9.toWords())
        assertEquals("ten",   10.toWords())
    }

    @Test
    fun `WHEN converting whole numbers between 10 and 19 to words THEN word representations are correct`() {
        assertEquals("ten",       10.toWords())
        assertEquals("eleven",    11.toWords())
        assertEquals("twelve",    12.toWords())
        assertEquals("thirteen",  13.toWords())
        assertEquals("fourteen",  14.toWords())
        assertEquals("fifteen",   15.toWords())
        assertEquals("sixteen",   16.toWords())
        assertEquals("seventeen", 17.toWords())
        assertEquals("eighteen",  18.toWords())
        assertEquals("nineteen",  19.toWords())
    }

    @Test
    fun `WHEN converting whole numbers between 20 and 29 to words THEN word representations are correct`() {
        assertEquals("twenty",       20.toWords())
        assertEquals("twenty-one",   21.toWords())
        assertEquals("twenty-two",   22.toWords())
        assertEquals("twenty-three", 23.toWords())
        assertEquals("twenty-four",  24.toWords())
        assertEquals("twenty-five",  25.toWords())
        assertEquals("twenty-six",   26.toWords())
        assertEquals("twenty-seven", 27.toWords())
        assertEquals("twenty-eight", 28.toWords())
        assertEquals("twenty-nine",  29.toWords())
    }

    @Test
    fun `WHEN converting whole numbers between 10 and 99 to words THEN word representations are correct`() {
        assertEquals("ten",           10.toWords())
        assertEquals("eleven",        11.toWords())
        assertEquals("twenty",        20.toWords())
        assertEquals("twenty-two",    22.toWords())
        assertEquals("thirty",        30.toWords())
        assertEquals("thirty-three",  33.toWords())
        assertEquals("forty",         40.toWords())
        assertEquals("forty-four",    44.toWords())
        assertEquals("fifty",         50.toWords())
        assertEquals("fifty-five",    55.toWords())
        assertEquals("sixty",         60.toWords())
        assertEquals("sixty-six",     66.toWords())
        assertEquals("seventy",       70.toWords())
        assertEquals("seventy-seven", 77.toWords())
        assertEquals("eighty",        80.toWords())
        assertEquals("eighty-eight",  88.toWords())
        assertEquals("ninety",        90.toWords())
        assertEquals("ninety-nine",   99.toWords())
    }

    @Test
    fun `WHEN converting whole numbers between 0 and 999 to words THEN word representations are correct`() {
        assertEquals("thirty",                      30.toWords())
        assertEquals("thirty-three",                33.toWords())
        assertEquals("forty",                       40.toWords())
        assertEquals("forty-four",                  44.toWords())
        assertEquals("fifty",                       50.toWords())
        assertEquals("fifty-five",                  55.toWords())
        assertEquals("sixty",                       60.toWords())
        assertEquals("sixty-six",                   66.toWords())
        assertEquals("seventy",                     70.toWords())
        assertEquals("seventy-seven",               77.toWords())
        assertEquals("eighty",                      80.toWords())
        assertEquals("eighty-eight",                88.toWords())
        assertEquals("ninety",                      90.toWords())
        assertEquals("ninety-nine",                 99.toWords())
        assertEquals("one hundred",                 100.toWords())
        assertEquals("one hundred one",             101.toWords())
        assertEquals("one hundred two",             102.toWords())
        assertEquals("one hundred three",           103.toWords())
        assertEquals("one hundred four",            104.toWords())
        assertEquals("one hundred five",            105.toWords())
        assertEquals("one hundred six",             106.toWords())
        assertEquals("one hundred seven",           107.toWords())
        assertEquals("one hundred eight",           108.toWords())
        assertEquals("one hundred nine",            109.toWords())
        assertEquals("one hundred ten",             110.toWords())
        assertEquals("one hundred eleven",          111.toWords())
        assertEquals("one hundred twelve",          112.toWords())
        assertEquals("one hundred thirteen",        113.toWords())
        assertEquals("one hundred fourteen",        114.toWords())
        assertEquals("one hundred fifteen",         115.toWords())
        assertEquals("one hundred sixteen",         116.toWords())
        assertEquals("one hundred seventeen",       117.toWords())
        assertEquals("one hundred eighteen",        118.toWords())
        assertEquals("one hundred nineteen",        119.toWords())
        assertEquals("one hundred twenty",          120.toWords())
        assertEquals("one hundred twenty-two",      122.toWords())
        assertEquals("one hundred thirty",          130.toWords())
        assertEquals("one hundred thirty-three",    133.toWords())
        assertEquals("one hundred forty",           140.toWords())
        assertEquals("one hundred forty-four",      144.toWords())
        assertEquals("one hundred fifty",           150.toWords())
        assertEquals("one hundred fifty-five",      155.toWords())
        assertEquals("one hundred sixty",           160.toWords())
        assertEquals("one hundred sixty-six",       166.toWords())
        assertEquals("one hundred seventy",         170.toWords())
        assertEquals("one hundred seventy-seven",   177.toWords())
        assertEquals("one hundred eighty",          180.toWords())
        assertEquals("one hundred eighty-eight",    188.toWords())
        assertEquals("one hundred ninety",          190.toWords())
        assertEquals("one hundred ninety-nine",     199.toWords())
        assertEquals("two hundred",                 200.toWords())
        assertEquals("two hundred twenty",          220.toWords())
        assertEquals("two hundred twenty-two",      222.toWords())
        assertEquals("three hundred",               300.toWords())
        assertEquals("three hundred thirty",        330.toWords())
        assertEquals("three hundred thirty-three",  333.toWords())
        assertEquals("four hundred",                400.toWords())
        assertEquals("four hundred forty",          440.toWords())
        assertEquals("four hundred forty-four",     444.toWords())
        assertEquals("five hundred",                500.toWords())
        assertEquals("five hundred fifty",          550.toWords())
        assertEquals("five hundred fifty-five",     555.toWords())
        assertEquals("six hundred",                 600.toWords())
        assertEquals("six hundred sixty",           660.toWords())
        assertEquals("six hundred sixty-six",       666.toWords())
        assertEquals("seven hundred",               700.toWords())
        assertEquals("seven hundred seventy",       770.toWords())
        assertEquals("seven hundred seventy-seven", 777.toWords())
        assertEquals("eight hundred",               800.toWords())
        assertEquals("eight hundred eighty",        880.toWords())
        assertEquals("eight hundred eighty-eight",  888.toWords())
        assertEquals("nine hundred",                900.toWords())
        assertEquals("nine hundred ninety",         990.toWords())
        assertEquals("nine hundred ninety-nine",    999.toWords())

        for (i in 0 until 1000) {
            assertFalse(i.toWords()!!.isBlank())
        }
    }

    @Test
    fun `WHEN converting whole numbers between 0 and -999 to words THEN word representations are correct`() {
        assertEquals("zero",                              0.toWords())
        assertEquals("negative one",                      (-1).toWords())
        assertEquals("negative two",                      (-2).toWords())
        assertEquals("negative three",                    (-3).toWords())
        assertEquals("negative four",                     (-4).toWords())
        assertEquals("negative five",                     (-5).toWords())
        assertEquals("negative six",                      (-6).toWords())
        assertEquals("negative seven",                    (-7).toWords())
        assertEquals("negative eight",                    (-8).toWords())
        assertEquals("negative nine",                     (-9).toWords())
        assertEquals("negative ten",                      (-10).toWords())
        assertEquals("negative eleven",                   (-11).toWords())
        assertEquals("negative one hundred",              (-100).toWords())
        assertEquals("negative one hundred ten",          (-110).toWords())
        assertEquals("negative one hundred eleven",       (-111).toWords())
        assertEquals("negative nine hundred ninety-nine", (-999).toWords())

        for (i in -1000 until 0) {
            assertFalse(i.toWords()!!.isBlank())
            assertTrue(i.toWords()!!.startsWith("negative"))
        }
    }

    @Test
    fun `WHEN converting whole numbers between 1000 and 999_999 to words THEN word representations are correct`() {
        assertEquals("one thousand",                        1000.toWords())
        assertEquals("one thousand one",                    1001.toWords())
        assertEquals("one thousand ten",                    1010.toWords())
        assertEquals("one thousand eleven",                 1011.toWords())
        assertEquals("one thousand one hundred",            1100.toWords())
        assertEquals("one thousand one hundred one",        1101.toWords())
        assertEquals("one thousand one hundred ten",        1110.toWords())
        assertEquals("one thousand one hundred eleven",     1111.toWords())
        assertEquals("two thousand",                        2000.toWords())
        assertEquals("two thousand two",                    2002.toWords())
        assertEquals("two thousand twenty",                 2020.toWords())
        assertEquals("two thousand twenty-two",             2022.toWords())
        assertEquals("two thousand two hundred",            2200.toWords())
        assertEquals("two thousand two hundred two",        2202.toWords())
        assertEquals("two thousand two hundred twenty",     2220.toWords())
        assertEquals("two thousand two hundred twenty-two", 2222.toWords())
        assertEquals("three thousand",                      3000.toWords())
        assertEquals("four thousand",                       4000.toWords())
        assertEquals("five thousand",                       5000.toWords())
        assertEquals("six thousand",                        6000.toWords())
        assertEquals("seven thousand",                      7000.toWords())
        assertEquals("eight thousand",                      8000.toWords())
        assertEquals("nine thousand",                       9000.toWords())
        assertEquals("ten thousand",                        10_000.toWords())
        assertEquals("eleven thousand",                     11_000.toWords())
        assertEquals("twelve thousand",                     12_000.toWords())
        assertEquals("thirteen thousand",                   13_000.toWords())
        assertEquals("fourteen thousand",                   14_000.toWords())
        assertEquals("fifteen thousand",                    15_000.toWords())
        assertEquals("sixteen thousand",                    16_000.toWords())
        assertEquals("seventeen thousand",                  17_000.toWords())
        assertEquals("eighteen thousand",                   18_000.toWords())
        assertEquals("nineteen thousand",                   19_000.toWords())
        assertEquals("twenty thousand",                     20_000.toWords())
        assertEquals("twenty-two thousand",                 22_000.toWords())
        assertEquals("thirty thousand",                     30_000.toWords())
        assertEquals("thirty-three thousand",               33_000.toWords())
        assertEquals("forty thousand",                      40_000.toWords())
        assertEquals("forty-four thousand",                 44_000.toWords())
        assertEquals("fifty thousand",                      50_000.toWords())
        assertEquals("fifty-five thousand",                 55_000.toWords())
        assertEquals("sixty thousand",                      60_000.toWords())
        assertEquals("sixty-six thousand",                  66_000.toWords())
        assertEquals("seventy thousand",                    70_000.toWords())
        assertEquals("seventy-seven thousand",              77_000.toWords())
        assertEquals("eighty thousand",                     80_000.toWords())
        assertEquals("eighty-eight thousand",               88_000.toWords())
        assertEquals("ninety thousand",                     90_000.toWords())
        assertEquals("ninety-nine thousand",                99_000.toWords())
        assertEquals("one hundred thousand",                                             100_000.toWords())
        assertEquals("one hundred eleven thousand one hundred eleven",                   111_111.toWords())
        assertEquals("two hundred thousand",                                             200_000.toWords())
        assertEquals("two hundred twenty-two thousand two hundred twenty-two",           222_222.toWords())
        assertEquals("three hundred thousand",                                           300_000.toWords())
        assertEquals("three hundred thirty-three thousand three hundred thirty-three",   333_333.toWords())
        assertEquals("four hundred thousand",                                            400_000.toWords())
        assertEquals("four hundred forty-four thousand four hundred forty-four",         444_444.toWords())
        assertEquals("five hundred thousand",                                            500_000.toWords())
        assertEquals("five hundred fifty-five thousand five hundred fifty-five",         555_555.toWords())
        assertEquals("six hundred thousand",                                             600_000.toWords())
        assertEquals("six hundred sixty-six thousand six hundred sixty-six",             666_666.toWords())
        assertEquals("seven hundred thousand",                                           700_000.toWords())
        assertEquals("seven hundred seventy-seven thousand seven hundred seventy-seven", 777_777.toWords())
        assertEquals("eight hundred thousand",                                           800_000.toWords())
        assertEquals("eight hundred eighty-eight thousand eight hundred eighty-eight",   888_888.toWords())
        assertEquals("nine hundred thousand",                                            900_000.toWords())
        assertEquals("nine hundred ninety-nine thousand nine hundred ninety-nine",       999_999.toWords())
    }

    @Test
    fun `WHEN converting whole numbers between one million and one decillion to words THEN word representations are correct`() {
        assertEquals("one million", 1_000_000.toWords())
        assertEquals("one million one", 1_000_001.toWords())
        assertEquals("one million ten", 1_000_010.toWords())
        assertEquals("one million eleven", 1_000_011.toWords())
        assertEquals("one million one hundred", 1_000_100.toWords())
        assertEquals("one million one hundred one", 1_000_101.toWords())
        assertEquals("one million one hundred ten", 1_000_110.toWords())
        assertEquals("one million one hundred eleven", 1_000_111.toWords())
        assertEquals("one million one thousand", 1_001_000.toWords())
        assertEquals("one million ten thousand", 1_010_000.toWords())
        assertEquals("one million eleven thousand", 1_011_000.toWords())
        assertEquals("one million one hundred thousand", 1_100_000.toWords())
        assertEquals("one million one hundred one thousand", 1_101_000.toWords())
        assertEquals("one million one hundred ten thousand", 1_110_000.toWords())
        assertEquals("one million one hundred eleven thousand", 1_111_000.toWords())


        assertEquals("two million", 2_000_000.toWords())

    }

    @Test
    fun `WHEN converting sequences of digits to individual number names THEN word sequence is correct`() {
        assertEquals("one two three four five six seven eight nine zero", 1234567890.toWords(true))
        assertEquals("five six six nine seven three nine four nine zero", 566_973_9490.toWords(true)) // Random phone number (https://www.randomlists.com/phone-numbers)
        assertEquals("one two three point three two one", 123.321.toWords(true))
        assertEquals("negative fourteen", (-14).toWords())
    }

    @Test
    fun `WHEN converting real numbers with fractional parts to words THEN word representations are correct`() {
        // Use BigDecimals to avoid floating point error
        assertEquals("zero point zero", 0.0.toWords())
        assertEquals("zero point one two five", 0.125f.toWords())
        assertEquals("one point zero one", "1.01".toBigDecimal().toWords())
        assertEquals("two point zero one two", "2.012".toBigDecimal().toWords())
        assertEquals("three point zero one two three", "3.0123".toBigDecimal().toWords())
        assertEquals("four point zero one two three four", "4.01234".toBigDecimal().toWords())
        assertEquals("five point zero one two three four five", "5.012345".toBigDecimal().toWords())
        assertEquals("six point zero one two three four five six", "6.0123456".toBigDecimal().toWords())
        assertEquals("seven point zero one two three four five six seven", "7.01234567".toBigDecimal().toWords())
        assertEquals("eight point zero one two three four five six seven eight", "8.012345678".toBigDecimal().toWords())
        assertEquals("nine point zero one two three four five six seven eight nine", "9.0123456789".toBigDecimal().toWords())
        assertEquals("ten point one two five", "10.125".toBigDecimal().toWords())
        assertEquals("eleven point two five", "11.25".toBigDecimal().toWords())
        assertEquals("twelve point three seven five", "12.375".toBigDecimal().toWords())
        assertEquals("thirteen point five", "13.5".toBigDecimal().toWords())
        assertEquals("twenty point six two five", "20.625".toBigDecimal().toWords())
        assertEquals("twenty-five point seven five", "25.75".toBigDecimal().toWords())
        assertEquals("one hundred forty-seven point eight seven five", "147.875".toBigDecimal().toWords())
        assertEquals("twelve thousand three hundred forty-five point five four three two one zero", "12345.543210".toBigDecimal().toWords())
        assertEquals("negative five point five", "-5.5".toBigDecimal().toWords())
        assertEquals("negative twenty-five point five two", "-25.52".toBigDecimal().toWords())
        assertEquals("negative one hundred twenty-five point five two one", "-125.521".toBigDecimal().toWords())
        assertEquals("negative one thousand two hundred fifty point zero five two one", "-1250.0521".toBigDecimal().toWords())
    }

    @Test
    fun `WHEN converting whole numbers between 1_000_000 and 1e30 to words THEN word representations are correct`() {
        assertEquals("one million",                                        1_000_000L.toWords())
        assertEquals("two million",                                        2_000_000L.toWords())
        assertEquals("three million",                                      3_000_000L.toWords())
        assertEquals("ten million",                                       10_000_000L.toWords())
        assertEquals("one hundred million",                              100_000_000L.toWords())
        assertEquals("one billion",                                    1_000_000_000L.toWords())
        assertEquals("ten billion",                                   10_000_000_000L.toWords())
        assertEquals("one hundred billion",                          100_000_000_000L.toWords())
        assertEquals("one trillion",                               1_000_000_000_000L.toWords())
        assertEquals("ten trillion",                              10_000_000_000_000L.toWords())
        assertEquals("one hundred trillion",                     100_000_000_000_000L.toWords())
        assertEquals("one quadrillion",                        1_000_000_000_000_000L.toWords())
        assertEquals("ten quadrillion",                       10_000_000_000_000_000L.toWords())
        assertEquals("one hundred quadrillion",              100_000_000_000_000_000L.toWords())
        assertEquals("one quintillion",                    1_000_000_000_000_000_000L.toWords())
        assertEquals("one sextillion",                "1_000_000_000_000_000_000_000".replace("_", "").toBigInteger().toWords())
        assertEquals("one septillion",            "1_000_000_000_000_000_000_000_000".replace("_", "").toBigInteger().toWords())
        assertEquals("one octillion",         "1_000_000_000_000_000_000_000_000_000".replace("_", "").toBigInteger().toWords())
        assertEquals("one nonillion",     "1_000_000_000_000_000_000_000_000_000_000".replace("_", "").toBigInteger().toWords())
    }

    @Test
    fun `WHEN converting whole numbers between one decillion and one nongentnonagintnovemillion to words THEN word representations are correct`() {
        assertEquals("one decillion",             ("1" + "0".repeat(33)).toBigInteger().toWords())
        assertEquals("ten decillion",             ("1" + "0".repeat(34)).toBigInteger().toWords())
        assertEquals("one hundred decillion",     ("1" + "0".repeat(35)).toBigInteger().toWords())
        assertEquals("one undecillion",           ("1" + "0".repeat(36)).toBigInteger().toWords())
        assertEquals("ten undecillion",           ("1" + "0".repeat(37)).toBigInteger().toWords())
        assertEquals("one hundred undecillion",   ("1" + "0".repeat(38)).toBigInteger().toWords())
        assertEquals("two duodecillion",          ("2" + "0".repeat(39)).toBigInteger().toWords())
        assertEquals("three tredecillion",        ("3" + "0".repeat(42)).toBigInteger().toWords())
        assertEquals("four quattuordecillion",    ("4" + "0".repeat(45)).toBigInteger().toWords())
        assertEquals("five quindecillion",        ("5" + "0".repeat(48)).toBigInteger().toWords())
        assertEquals("six sexdecillion",          ("6" + "0".repeat(51)).toBigInteger().toWords())
        assertEquals("seven septendecillion",     ("7" + "0".repeat(54)).toBigInteger().toWords())
        assertEquals("eight octodecillion",       ("8" + "0".repeat(57)).toBigInteger().toWords())
        assertEquals("nine novemdecillion",       ("9" + "0".repeat(60)).toBigInteger().toWords())
        assertEquals("one vigintillion",          ("1" + "0".repeat(63)).toBigInteger().toWords())
        assertEquals("one unvigintillion",        ("1" + "0".repeat(66)).toBigInteger().toWords())
        assertEquals("one duovigintillion",       ("1" + "0".repeat(69)).toBigInteger().toWords())
        assertEquals("one trevigintillion",       ("1" + "0".repeat(72)).toBigInteger().toWords())
        assertEquals("one quattuorvigintillion",  ("1" + "0".repeat(75)).toBigInteger().toWords())
        assertEquals("one quinvigintillion",      ("1" + "0".repeat(78)).toBigInteger().toWords())
        // Number of atoms in the observable universe is around here
        assertEquals("one sexvigintillion",       ("1" + "0".repeat(81)).toBigInteger().toWords())
        assertEquals("one septenvigintillion",    ("1" + "0".repeat(84)).toBigInteger().toWords())
        assertEquals("one octovigintillion",      ("1" + "0".repeat(87)).toBigInteger().toWords())
        assertEquals("one novemvigintillion",     ("1" + "0".repeat(90)).toBigInteger().toWords())
        assertEquals("one trigintillion",         ("1" + "0".repeat(93)).toBigInteger().toWords())
        assertEquals("one tretrigintillion",      ("1" + "0".repeat(102)).toBigInteger().toWords())
        assertEquals("one quadragintillion",      ("1" + "0".repeat(123)).toBigInteger().toWords())
        assertEquals("one quinquagintillion",     ("1" + "0".repeat(153)).toBigInteger().toWords())
        assertEquals("one sexagintillion",        ("1" + "0".repeat(183)).toBigInteger().toWords())
        assertEquals("one septagintillion",       ("1" + "0".repeat(213)).toBigInteger().toWords())
        assertEquals("one octogintillion",        ("1" + "0".repeat(243)).toBigInteger().toWords())
        assertEquals("one nonagintillion",        ("1" + "0".repeat(273)).toBigInteger().toWords())
        assertEquals("one centillion",            ("1" + "0".repeat(303)).toBigInteger().toWords())
        assertEquals("one uncentillion",          ("1" + "0".repeat(306)).toBigInteger().toWords())
        assertEquals("one duocentillion",         ("1" + "0".repeat(309)).toBigInteger().toWords())
        assertEquals("one trecentillion",         ("1" + "0".repeat(312)).toBigInteger().toWords())
        assertEquals("one quattuorcentillion",    ("1" + "0".repeat(315)).toBigInteger().toWords())
        assertEquals("one quincentillion",        ("1" + "0".repeat(318)).toBigInteger().toWords())
        assertEquals("one sexcentillion",         ("1" + "0".repeat(321)).toBigInteger().toWords())
        assertEquals("one septencentillion",      ("1" + "0".repeat(324)).toBigInteger().toWords())
        assertEquals("one octocentillion",        ("1" + "0".repeat(327)).toBigInteger().toWords())
        assertEquals("one novemcentillion",       ("1" + "0".repeat(330)).toBigInteger().toWords())
        assertEquals("one deccentillion",         ("1" + "0".repeat(333)).toBigInteger().toWords())
        assertEquals("one undeccentillion",       ("1" + "0".repeat(336)).toBigInteger().toWords())
        assertEquals("one vigintcentillion",      ("1" + "0".repeat(363)).toBigInteger().toWords())
        assertEquals("one trigintcentillion",     ("1" + "0".repeat(393)).toBigInteger().toWords())
        assertEquals("one quadragintcentillion",  ("1" + "0".repeat(423)).toBigInteger().toWords())
        assertEquals("one quinquagintcentillion", ("1" + "0".repeat(453)).toBigInteger().toWords())
        assertEquals("one sexagintcentillion",    ("1" + "0".repeat(483)).toBigInteger().toWords())
        assertEquals("one septagintcentillion",   ("1" + "0".repeat(513)).toBigInteger().toWords())
        assertEquals("one octogintcentillion",    ("1" + "0".repeat(543)).toBigInteger().toWords())
        assertEquals("one nonagintcentillion",    ("1" + "0".repeat(573)).toBigInteger().toWords())
        assertEquals("one ducentillion",          ("1" + "0".repeat(603)).toBigInteger().toWords())
        assertEquals("one trecentillion",         ("1" + "0".repeat(903)).toBigInteger().toWords())
        assertEquals("one quadringentillion",     ("1" + "0".repeat(1203)).toBigInteger().toWords())
        assertEquals("one quingentillion",        ("1" + "0".repeat(1503)).toBigInteger().toWords())
        assertEquals("one sescentillion",         ("1" + "0".repeat(1803)).toBigInteger().toWords())
        assertEquals("one septingentillion",      ("1" + "0".repeat(2103)).toBigInteger().toWords())
        assertEquals("one octingentillion",       ("1" + "0".repeat(2403)).toBigInteger().toWords())
        assertEquals("one nongentillion",         ("1" + "0".repeat(2703)).toBigInteger().toWords())
        assertEquals("one novemnonagintnongentillion", ("1" + "0".repeat(3000)).toBigInteger().toWords())
    }
}
