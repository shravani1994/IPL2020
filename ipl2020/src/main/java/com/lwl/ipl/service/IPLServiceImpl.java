package com.lwl.ipl.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.lwl.ipl.domain.Player;
import com.lwl.ipl.domain.Team;
import com.lwl.ipl.dto.PlayerDTO;
import com.lwl.ipl.dto.RoleCountDTO;
import com.lwl.ipl.dto.TeamAmountByRoleDTO;
import com.lwl.ipl.dto.TeamAmountDTO;
import com.lwl.ipl.dto.TeamDTO;
import com.lwl.ipl.util.JsonReaderUtil;

public class IPLServiceImpl implements IPLService {

	private List<Team> teams = JsonReaderUtil.readJson("src/main/resources/ipl2020.json");

	ModelMapper modelMapper = new ModelMapper();

	@Override
	public List<String> getTeamLabels() {

		List<String> teamLabels = teams.stream().map(Team::getLabel).collect(Collectors.toList());
		teamLabels.forEach(t -> System.out.println(t));
		return teamLabels;
	}

	@Override
	public List<PlayerDTO> getPlayerByTeam(String label) {

		List<PlayerDTO> playerByTeam = teams.stream().filter(t -> t.getLabel().equalsIgnoreCase(label))
				.flatMap(team -> team.getPlayers().stream()).map(p -> modelMapper.map(p, PlayerDTO.class))
				.collect(Collectors.toList());
		return playerByTeam;
	}

	@Override
	public List<RoleCountDTO> getCountByRole(String label) {

		Map<String, Long> roleCountMap = teams.stream().filter(t -> label.equalsIgnoreCase(t.getLabel()))
				.flatMap(t -> t.getPlayers().stream())
				.collect(Collectors.groupingBy(Player::getRole, Collectors.counting()));

		List<RoleCountDTO> roleCounts = new ArrayList<RoleCountDTO>();

		for (Map.Entry<String, Long> entry : roleCountMap.entrySet()) {
			roleCounts.add(new RoleCountDTO(entry.getKey(), entry.getValue()));
		}
		return roleCounts;
	}

	@Override
	public List<PlayerDTO> getPlayerByTeamAndRole(String label, String role) {

		List<PlayerDTO> playerByTeamAndRole = teams.stream().filter(t -> t.getLabel().equalsIgnoreCase(label))
				.flatMap(team -> team.getPlayers().stream()).filter(p -> role.equalsIgnoreCase(p.getRole()))
				.map(p -> modelMapper.map(p, PlayerDTO.class)).collect(Collectors.toList());

		return playerByTeamAndRole;
	}

	@Override
	public List<TeamDTO> getAllTeamDetails() {
		List<TeamDTO> teamDetails = teams.stream().map(t -> modelMapper.map(t, TeamDTO.class))
				.collect(Collectors.toList());

		return teamDetails;
	}

	@Override
	public List<TeamAmountDTO> getAmountSpentByTeams() {

		Map<String, Long> teamAmountMap = teams.stream().collect(Collectors.groupingBy(Team::getLabel,
				Collectors.summingLong(t -> t.getPlayers().stream().mapToLong(p -> p.getPrice()).sum())));

		List<TeamAmountDTO> teamAmounts = new ArrayList<TeamAmountDTO>();

		for (Map.Entry<String, Long> entry : teamAmountMap.entrySet()) {
			teamAmounts.add(new TeamAmountDTO(entry.getKey(), entry.getValue()));
		}

		return teamAmounts;
	}

	@Override
	public List<TeamAmountByRoleDTO> getAmountSpentByTeamAndRole(String label, String role) {

		Long price = teams.stream().filter(t -> label.equalsIgnoreCase(t.getLabel()))
				.flatMap(p -> p.getPlayers().stream()).filter(p -> role.equalsIgnoreCase(p.getRole()))
				.mapToLong(p -> p.getPrice()).sum();

		List<TeamAmountByRoleDTO> teamAmountByRoles = new ArrayList<TeamAmountByRoleDTO>();

		teamAmountByRoles.add(new TeamAmountByRoleDTO(label, role, price));

		return teamAmountByRoles;
	}

	@Override
	public List<PlayerDTO> getPlayersBySort(String fieldName) {

		Comparator<Player> roleComparator = (p1, p2) -> p1.getRole().compareTo(p2.getRole());
		Comparator<Player> priceComparator = (p1, p2) -> p1.getPrice().compareTo(p2.getPrice());
		Comparator<Player> playerComparator = (p1, p2) -> p1.getPlayer().compareTo(p2.getPlayer());
		Comparator<Player> comparatorToUse = roleComparator;

		if (fieldName.equalsIgnoreCase("role")) {
			comparatorToUse = roleComparator;
		} else if (fieldName.equalsIgnoreCase("price")) {
			comparatorToUse = priceComparator;
		} else if (fieldName.equalsIgnoreCase("player")) {
			comparatorToUse = playerComparator;
		}

		List<PlayerDTO> playersBySort = teams.stream().flatMap(t -> t.getPlayers().stream()).sorted(comparatorToUse)
				.map(p -> modelMapper.map(p, PlayerDTO.class)).collect(Collectors.toList());
		playersBySort.forEach(p -> System.out.println(p.toString()));
		return playersBySort;
	}

	@Override
	public List<PlayerDTO> getMaxPaidPlayersByRole() {

		List<PlayerDTO> maxPaidPlayers = teams.stream().flatMap(p -> p.getPlayers().stream())
				.collect(Collectors.groupingBy(Player::getRole,
						Collectors.maxBy(Comparator.comparing(Player::getPrice))))
				.values().stream().map(Optional::get).map(p -> modelMapper.map(p, PlayerDTO.class))
				.collect(Collectors.toList());

		return maxPaidPlayers;
	}

}
