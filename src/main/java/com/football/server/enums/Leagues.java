package com.football.server.enums;

public enum Leagues {
	EPL(445);
	private final int leagueCode;
	
	Leagues(int leagueCode) {
		this.leagueCode = leagueCode;
	}
	
	public int getLeagueCode() {
		return leagueCode;
	}
	
}
