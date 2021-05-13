package com.lwl.ipl.service;

import java.util.List;

import com.lwl.ipl.dto.PlayerDTO;
import com.lwl.ipl.dto.RoleCountDTO;
import com.lwl.ipl.dto.TeamAmountByRoleDTO;
import com.lwl.ipl.dto.TeamAmountDTO;
import com.lwl.ipl.dto.TeamDTO;

public interface IPLService {
	
	public List<String> getTeamLabels();
	public List<PlayerDTO> getPlayerByTeam(String label);
	public List<RoleCountDTO> getCountByRole(String label);
	public List<PlayerDTO> getPlayerByTeamAndRole(String label, String role);
	public List<TeamDTO> getAllTeamDetails();
	public List<TeamAmountDTO> getAmountSpentByTeams();
	public List<TeamAmountByRoleDTO> getAmountSpentByTeamAndRole(String label,String role);
	public List<PlayerDTO> getPlayersBySort(String fieldName);
	public List<PlayerDTO> getMaxPaidPlayersByRole();

}
