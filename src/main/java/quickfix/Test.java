package quickfix;

import java.time.LocalDate;
import java.time.YearMonth;

public class Test {

	public static void main(String[] args) {
		LocalDate day = LocalDate.now();
		LocalDate startDate = day.minusMonths(3 - 1).withDayOfMonth(1);
		System.out.println("Start :" + startDate + " - " + getYearMonth(startDate) + " - " +  getYearMonth(startDate.minusDays(1)));
		System.out.println("End :" + day + " - " + getYearMonth(day));
		
		System.out.println(getMonthsAgo(day, 3));
		
	}
	
	 public static Integer getYearMonth(LocalDate date) {
        YearMonth ref = YearMonth.from(date);
        return ref.getYear() * 100 + ref.getMonthValue();
    }
	 
	 
	 public static int getMonthsAgo(LocalDate ref, int n) {
        YearMonth ym = YearMonth.from(ref).minusMonths(n);
        return ym.getYear() * 100 + ym.getMonthValue();
    }
}
