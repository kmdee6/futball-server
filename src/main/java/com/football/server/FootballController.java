package com.football.server;

import java.io.IOException;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.football.server.constants.Constants;
import com.football.server.enums.Leagues;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@RestController
@CrossOrigin
public class FootballController {
	
	@RequestMapping("/teams")
	public TeamList getTeams() throws JsonParseException, JsonMappingException, IOException {
		
		HttpResponse<JsonNode> response = null;
    	try {
			/*response = Unirest.get("https://heisenbug-premier-league-live-scores-v1.p.mashape.com/api/premierleague/table")
					.header("X-Mashape-Key", "biuz5Sx3mCmshhottp7FNQq1P4WXp1Yh7CWjsnsg9g0IHWvNPF")
					.header("Accept", "application/json")
					.asJson();*/
    		response = Unirest.get(Constants.URL_TEAMS.replace("{id}", String.valueOf(Leagues.EPL.getLeagueCode())))
					.header("X-Auth-Token", Constants.API_KEY)
					.header("X-Response-Control", "minified")
					.header("Accept", "application/json")
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
    	ObjectMapper mapper = new ObjectMapper();
    	TeamList teams = mapper.readValue(response.getBody().toString(), TeamList.class);
    	for(Team team: teams.getTeams()) {
    		System.err.println(team.getName());
    	}
    	return teams;
    	
	}

    @RequestMapping("/players")
    public HttpResponse<JsonNode> getPlayers(@RequestParam(name="team")String team) {
    	HttpResponse<JsonNode> response = null;
    	try {
    		String processedName = processTeam(team);
			response = Unirest.get("https://heisenbug-premier-league-live-scores-v1.p.mashape.com/api/premierleague/players?team=" + processedName)
					.header("X-Mashape-Key", "biuz5Sx3mCmshhottp7FNQq1P4WXp1Yh7CWjsnsg9g0IHWvNPF")
					.header("Accept", "application/json")
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
    	System.out.println(response.getBody());
        return response;
    }
    
    private String processTeam(String teamName) {
    	return StringUtils.isEmpty(teamName)? "" : teamName.replace(" ", "+");
    }
}
