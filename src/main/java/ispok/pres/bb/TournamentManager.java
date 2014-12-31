/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ispok.pres.bb;

import ispok.dto.LevelDto;
import ispok.dto.OfficeDto;
import ispok.dto.PayoutPlaceDto;
import ispok.dto.PayoutStructureDto;
import ispok.dto.TournamentDto;
import ispok.dto.VisitorDto;
import ispok.helper.FacesUtil;
import ispok.pokerclock.ActiveTournametsHolder;
import ispok.pokerclock.TournamentController;
import ispok.service.OfficeService;
import ispok.service.PayoutStructureService;
import ispok.service.TournamentService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jan Mucha <j.mucha@seznam.cz>
 */
@Component
@SessionScoped
public class TournamentManager {

    @Autowired
    private OfficeService officeService;
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private ActiveTournametsHolder activeTournametsHolder;
    @Autowired
    private LazyDataModel<VisitorDto> visitorLazyDataModel;
    @Autowired
    private PayoutStructureService payoutStructureService;

    private Long officeId;
    private Long tournamentId;
    private List<TournamentDto> activeTournaments;
//    private TournamentDto tournament;
    private TournamentController tournamentController;
    private PayoutStructureDto payoutStructureDto;
    private List<PayoutPlaceDto> payoutPlaces;

    private Long payoutStructureId;
    private List<VisitorDto> filteredVisitors;
    private VisitorDto selectedVisitor;

    private boolean showPokerClock;

    private float prizePoll;

    @PostConstruct
    private void init() {
        tournamentController = null;
        activeTournaments = new ArrayList<>(20);
        officeId = (long) 0;
        tournamentId = (long) 0;
        payoutPlaces = new ArrayList<>(20);
        showPokerClock = false;
    }

    /**
     * Get the value of payoutStructureId
     *
     * @return the value of payoutStructureId
     */
    public Long getPayoutStructureId() {
        return payoutStructureId;
    }

    /**
     * Set the value of payoutStructureId
     *
     * @param payoutStructureId new value of payoutStructureId
     */
    public void setPayoutStructureId(Long payoutStructureId) {
        this.payoutStructureId = payoutStructureId;

    }

    /**
     * Get the value of levelTime
     *
     * @return the value of levelTime
     */
    public String getLevelTime() {
        return null;
    }

    /**
     * Get the value of tournamentId
     *
     * @return the value of tournamentId
     */
    public Long getTournamentId() {
        return tournamentId;
    }

    /**
     * Set the value of tournamentId
     *
     * @param tournamentId new value of tournamentId
     */
    public void setTournamentId(Long id) {
        this.tournamentId = id;
        if (tournamentId != 0) {
            tournamentController = activeTournametsHolder.loadTournament(tournamentId);
        }
    }

    /**
     * Get the value of officeId
     *
     * @return the value of officeId
     */
    public Long getOfficeId() {
        return officeId;
    }

    /**
     * Set the value of officeId
     *
     * @param officeId new value of officeId
     */
    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
//        List<TournamentDto> allTournaments = tournamentService.getAll();
//        activeTournaments.clear();
//        for (TournamentDto tdto : allTournaments) {
//            if (tdto.getPlaceId().equals(officeId) && tdto.getFinish() == null) {
//                activeTournaments.add(tdto);
//            }
//        }
        List<TournamentDto> allTournaments = tournamentService.getAll();
        activeTournaments.clear();
        for (TournamentDto tdto : allTournaments) {
            if (tdto.getPlaceId().equals(officeId) && tdto.getFinish() == null) {
                activeTournaments.add(tdto);
            }
        }
        if (activeTournaments.isEmpty()) {
            tournamentId = new Long(0);
            tournamentController = null;
            tournamentId = (long) 0;
            payoutPlaces = new ArrayList<>(20);
            showPokerClock = false;
        } else {
            setTournamentId(activeTournaments.get(0).getId());
            showPokerClock = true;
        }
    }

    public List<OfficeDto> getAllOffices() {
        List<OfficeDto> officeDtos = officeService.getAll();
        if (officeDtos != null && !officeDtos.isEmpty()) {
            setOfficeId(officeDtos.get(0).getId());
        }
        return officeDtos;
    }

    public List<TournamentDto> getActiveTournaments() {

        return activeTournaments;
    }

//    public TournamentController getTournamentController() {
//        return tournamentController;
//    }
    public int getPlayersCount() {
        if (tournamentController != null) {
            return tournamentController.getPlayers().size();
        }
        return 0;
    }

    public String getTime() {
        if (tournamentController == null) {
            return "0";
        }
        int time_s = tournamentController.getTime_s();
        int minutes = time_s / 60;
        int seconds = time_s % 60;
        return minutes + ":" + seconds;
    }

    public int getLevelNumber() {
        if (tournamentController == null) {
            return 0;
        }
        return tournamentController.getCurrentLevel().getNumber();
    }

    public LevelDto getLevelDto() {
        if (tournamentController == null) {
            return new LevelDto();
        }
        return tournamentController.getCurrentLevel();
    }

    public LazyDataModel<VisitorDto> getVisitorLazyDataModel() {
        return visitorLazyDataModel;
    }

    /**
     * Get the value of filteredVisitors
     *
     * @return the value of filteredVisitors
     */
    public List<VisitorDto> getFilteredVisitors() {
        return filteredVisitors;
    }

    /**
     * Set the value of filteredVisitors
     *
     * @param filteredVisitors new value of filteredVisitors
     */
    public void setFilteredVisitors(List<VisitorDto> filteredVisitors) {
        this.filteredVisitors = filteredVisitors;
    }

    /**
     * Get the value of selectedVisitor
     *
     * @return the value of selectedVisitor
     */
    public VisitorDto getSelectedVisitor() {
        return selectedVisitor;
    }

    /**
     * Set the value of selectedVisitor
     *
     * @param selectedVisitor new value of selectedVisitor
     */
    public void setSelectedVisitor(VisitorDto selectedVisitor) {
        this.selectedVisitor = selectedVisitor;
    }

    public void addPlayer(Long id) {
        if (tournamentController == null) {
            FacesUtil.addWarnMessage("warn", "warn");
            return;
        }
        if (tournamentController.containsPlayer(id)) {
            FacesUtil.addWarnMessage("warn", "tournament_player_already_in");
        } else {
            tournamentController.addPlayer(id);
            FacesUtil.addInfoMessage("success", "player_added");
        }
    }

    public void removePlayer(Long id) {
        if (tournamentController == null) {
            FacesUtil.addWarnMessage("warn", "warn");
            return;
        }
        tournamentController.removePlayer(id);
        FacesUtil.addInfoMessage("success", "player_removed");
    }

    public int getEntries() {
        if (tournamentController == null) {
            return 0;
        }
        return tournamentController.getEntries();
    }

    public float getBuyin() {
        if (tournamentController == null) {
            return 0;
        }
        return tournamentController.getTournamentDto().getBuyin();
    }

    public float getAddon() {
        if (tournamentController == null) {
            return 0;
        }
        return tournamentController.getTournamentDto().getAddon();
    }

    public Date getLateReg() {
        if (tournamentController == null) {
            return null;
        }
        return tournamentController.getTournamentDto().getLateReg();
    }

    public String getInfo() {
        if (tournamentController == null) {
            return "";
        }
        return tournamentController.getTournamentDto().getInfo();
    }

    public List<VisitorDto> getPlayers() {
        if (tournamentController == null) {
            return null;
        }
        return tournamentController.getPlayers();
    }

    public void start() {
        if (tournamentController == null) {
            FacesUtil.addWarnMessage("warn", "warn");
            return;
        }
        tournamentController.start();
    }

    public void pause() {
        if (tournamentController == null) {
            FacesUtil.addWarnMessage("warn", "warn");
            return;
        }
        tournamentController.pause();
    }

    public void prevLevel() {
        if (tournamentController == null) {
            FacesUtil.addWarnMessage("warn", "warn");
            return;
        }
        tournamentController.setPrevCounter();
    }

    public void nextLevel() {
        if (tournamentController == null) {
            FacesUtil.addWarnMessage("warn", "warn");
            return;
        }
        tournamentController.setNextCounter();
    }

    public boolean isBreak() {
        if (tournamentController == null) {
            return false;
        }
        return tournamentController.isLevelBreak();
    }

    public void loadPayouts() {
        if (tournamentController == null) {
            return;
        }
        payoutPlaces.clear();
        try {
            payoutPlaces = new ArrayList<>(tournamentController.getPayoutPlaces());
            TournamentDto tournamentDto = tournamentController.getTournamentDto();
            payoutStructureId = tournamentDto.getPayoutStructureId();
            prizePoll = tournamentController.getPrizePool();
        } catch (NullPointerException e) {
            prizePoll = 0;
        }
    }

    public List<PayoutPlaceDto> getPayoutPlaces() {
        return payoutPlaces;
    }

    public List<PayoutStructureDto> getPayoutStructures() {
        return payoutStructureService.getAll();
    }

    public float getEntryPrizePool() {
        if (tournamentController == null) {
            return 0;
        }
        try {
            TournamentDto tournamentDto = tournamentController.getTournamentDto();
            return tournamentController.getEntries() * tournamentDto.getBuyin();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public float getPrizePoll() {
        return prizePoll;
    }

    public void setPrizePoll(float prizePoll) {
        this.prizePoll = prizePoll;
    }

    public void loadPayoutStructure() {
        payoutPlaces.clear();
        payoutStructureDto = payoutStructureService.getStructureById(payoutStructureId);
        for (Long l : payoutStructureDto.getPayoutPlaceIds()) {
            payoutPlaces.add(payoutStructureService.getPlaceById(l));
        }
        calculatePayouts();
    }

    public void calculatePayouts() {
        for (PayoutPlaceDto payoutPlaceDto : payoutPlaces) {
            payoutPlaceDto.setMoney(payoutPlaceDto.getPercent() * prizePoll / 100);
        }
    }

    public void calculatePayoutsPercent() {
        float sum = getPayoutSum();
        for (PayoutPlaceDto placeDto : payoutPlaces) {
            float money = placeDto.getMoney();
            if (sum == 0) {
                placeDto.setPercent(0);
            } else {
                placeDto.setPercent(placeDto.getMoney() / sum * 100);
            }
        }
        prizePoll = sum;
    }

    public float getPayoutSum() {
        float sum = 0;
        for (PayoutPlaceDto placeDto : payoutPlaces) {
            sum += placeDto.getMoney();
        }
        return sum;
    }

    public void removePayoutPlace(int placeNumber) {
        if (payoutPlaces.size() == 1) {
            PayoutPlaceDto payoutPlaceDto = payoutPlaces.get(0);
            payoutPlaceDto.setPlace(1);
            payoutPlaceDto.setPercent(0);
            payoutPlaceDto.setMoney(0);
        }
        for (int i = 0; i < payoutPlaces.size(); i++) {
            PayoutPlaceDto payoutPlaceDto = payoutPlaces.get(i);
            if (payoutPlaceDto.getPlace() == placeNumber) {
                payoutPlaces.remove(i);
                for (; i < payoutPlaces.size(); i++) {
                    payoutPlaces.get(i).setPlace(i + 1);
                }
                return;
            }
        }
    }

    public void addPayoutPlace(int placeNumber) {
        PayoutPlaceDto payoutPlaceDto = new PayoutPlaceDto();
        payoutPlaceDto.setPlace(placeNumber + 1);

        if (placeNumber >= payoutPlaces.size()) {
            payoutPlaces.add(payoutPlaceDto);
            return;
        }
        payoutPlaces.add(placeNumber, payoutPlaceDto);

        for (int i = placeNumber + 1; i < payoutPlaces.size(); i++) {
            payoutPlaces.get(i).setPlace(i + 1);
        }
    }

    public void savePayouts() {
        if (tournamentController == null) {
            FacesUtil.addWarnMessage("warn", "warn");
            return;
        }
        tournamentController.setPayoutPlaces(new ArrayList<PayoutPlaceDto>(payoutPlaces));
        tournamentController.setPrizePool(prizePoll);
        FacesUtil.addInfoMessage("success", "payouts_saved");
    }

    public void sitout(Long playerId) {
        if (tournamentController == null) {
            FacesUtil.addWarnMessage("warn", "warn");
            return;
        }
        tournamentController.sitoutPlayer(playerId);
        FacesUtil.addInfoMessage("success", "player_sitout");
    }

    public boolean isShowPokerClock() {
        return showPokerClock;
    }
}
