package com.lwl.ipl.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class Team {
	private String city;
	private String coach;
	private String home;
	private String team;
	private String label;
	private List<Player> players;

}
