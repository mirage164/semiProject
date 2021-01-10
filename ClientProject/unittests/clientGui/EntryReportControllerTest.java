package clientGui;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.sun.corba.se.pept.transport.Connection;

import clientIO.ConnectionInterface;
import entities.ParkEntry;
import entities.ParkEntry.EntryType;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modules.ServerRequest;

class EntryReportControllerTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	private EntryReportController entryReportController;

	private ParkEntry[] entries;
	private Timestamp[] dates;
	private ConnectionInterface connection;// TODO need to be local?

	private JFXPanel panel = new JFXPanel();

	public double[] expectedAvgStayArray;
	public int[] expectedTotalPeopleType;

	public int[][] expectedSumPeople;

	private class ConnectionSutb implements ConnectionInterface {

		@Override
		public String sendRequestAndResponse(ServerRequest request) {
			return ServerRequest.gson.toJson(entries, ParkEntry[].class);
		}

	}

	@BeforeEach
	void setUp() throws Exception {

		// set stub server connection
		connection = new ConnectionSutb();

		// entryReportController = new EntryReportController();

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(EntryReportController.class.getResource("EntryReport.fxml"));
		loader.load();

		entryReportController = loader.getController();
		entryReportController.setServerConnection(connection);

		// set relevent dates
		LocalDate localDate = LocalDate.of(2021, 1, 1);
		Timestamp fromDate = Timestamp.valueOf(localDate.atStartOfDay());
		Timestamp toDate = Timestamp.valueOf(localDate.atStartOfDay().plusDays(1));
		dates = new Timestamp[2];
		dates[0] = fromDate;
		dates[1] = toDate;
		entries = new ParkEntry[4];
		// set parkentries for checking
		entries[0] = new ParkEntry(EntryType.Personal, "1", "park1", fromDate,
				Timestamp.valueOf(fromDate.toLocalDateTime().plusHours(1)), 1, 0, false, 10);
		entries[1] = new ParkEntry(EntryType.Subscriber, "2", "park1", fromDate,
				Timestamp.valueOf(fromDate.toLocalDateTime().plusHours(2)), 1, 1, false, 20);
		entries[2] = new ParkEntry(EntryType.Group, "3", "park1", fromDate,
				Timestamp.valueOf(fromDate.toLocalDateTime().plusHours(3)), 10, 10, false, 100);

		entries[3] = new ParkEntry(EntryType.PrivateGroup, "4", "park1", fromDate,
				Timestamp.valueOf(fromDate.toLocalDateTime().plusHours(4)), 10, 5, false, 200);

		expectedAvgStayArray = new double[ParkEntry.EntryType.values().length];
		expectedTotalPeopleType = new int[ParkEntry.EntryType.values().length];
		for (int i = 0; i < 4; i++) {
			expectedAvgStayArray[i] = 0;
			expectedTotalPeopleType[i] = 0;
		}

	}

	/**
	 * <pre>
	 * test for getting currect calculation for avarage time of stay for each type
	 * input: parkName:Gold, parkID:park1, dates:{1.1.2021,2.1.2021}
	 * expected: expectedAvgStayArray[i]=entryReportController.avgStayArray[i] for each i
	 * </pre>
	 */
	@Test
	void testAvgStayCurrectParkEntries() {

		expectedAvgStayArray[0] = 1;
		expectedAvgStayArray[1] = 2;
		expectedAvgStayArray[2] = 3;
		expectedAvgStayArray[3] = 4;

		entryReportController.initReport("Gold", "park1", dates);
		assertArrayEquals(expectedAvgStayArray, entryReportController.avgStayArray);

	}

	/**
	 * <pre>
	 * test for getting currect calculation for total sum of people stay for each type
	 * input: parkName:Gold, parkID:park1, dates:{1.1.2021,2.1.2021}
	 * expected: expectedTotalPeopleType[i]=entryReportController.totalPeopleType[i] for each i
	 * </pre>
	 */
	@Test
	void testTotalPeopleOfTypeCurrectParkEntries() {
		expectedTotalPeopleType[0] = 1;
		expectedTotalPeopleType[1] = 1;
		expectedTotalPeopleType[2] = 10;
		expectedTotalPeopleType[3] = 10;
		entryReportController.initReport("Gold", "park1", dates);
		assertArrayEquals(expectedTotalPeopleType, entryReportController.totalPeopleType);

	}

	/**
	 * <pre>
	 * test if all entries was recived
	 * input: parkName:Gold, parkID:park1, dates:{1.1.2021,2.1.2021}
	 * expected: all entries exist
	 * </pre>
	 */
	@Test
	void testGotAllParkEntries() {

		entryReportController.initReport("Gold", "park1", dates);
		assertEquals(entries.length, entries.length);

	}

	
	
	
	
	
}
