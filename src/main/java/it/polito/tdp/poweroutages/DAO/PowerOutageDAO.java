package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.Outage;

public class PowerOutageDAO {
	
	public PowerOutageDAO() {
		this.nercMap = new HashMap<>();
	}

	Map<Nerc, Integer> nercMap;
	
	public List<Nerc> getNercList() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
				this.nercMap.put(n, res.getInt("id"));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}

	public Map<Nerc, Integer> getNercMap() {
		return nercMap;
	}

	public void setNercMap(Map<Nerc, Integer> nercMap) {
		this.nercMap = nercMap;
	}
	
	public List<Outage> getOutages(Nerc n) {
		
		String sql = "SELECT id, customers_affected, date_event_began AS \"dataInizio\", date_event_finished AS dataFine, TIMEDIFF (date_event_finished,date_event_began) AS \"oreDisservizio\" "
				+ "FROM poweroutages "
				+ "WHERE nerc_id = ?";
		
		List<Outage> outageList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			int nercId = this.nercMap.get(n);
			st.setInt(1, nercId);
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Outage o = new Outage(res.getInt("id"), res.getInt("customers_affected"),
						res.getDate("dataInizio"), res.getDate("dataFine"), res.getTime("oreDisservizio"));
				outageList.add(o);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return outageList;
		
	}

}
