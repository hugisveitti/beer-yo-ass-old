package project.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.PendingBet;
import project.persistence.entities.Bet;
import project.persistence.entities.User;
import project.persistence.repositories.BetRepository;
import project.persistence.repositories.PendingBetRepository;
import project.service.BetService;
import project.service.CustomUserDetailsService;

import java.util.HashSet;
import java.util.Set;
import java.time.ZonedDateTime;

@Service
public class BetServiceImplementation implements BetService {

    private PendingBetRepository pendingBetRepository;
    private BetRepository betRepository;
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public BetServiceImplementation(PendingBetRepository pendingBetRepository, BetRepository betRepository, CustomUserDetailsService customUserDetailsService){
        this.pendingBetRepository = pendingBetRepository;
        this.betRepository = betRepository;
        this.customUserDetailsService = customUserDetailsService;
    }

    /*
    Pending bet service implementation
     */

    //ma kannski eyda thessum classa
    @Override
    public void savePendingBet(PendingBet pendingBet){
         pendingBetRepository.save(pendingBet);
    }



    @Override
    public void savePendingBet(PendingBet pendingBet, User sender, User receiver) throws Exception{

        if(sender.getCredit() < pendingBet.getAmountSender()){
            throw new Exception("Sender does not have enough credits");
        }

        sender.removeCredit(pendingBet.getAmountSender());

        //TODO laga
        pendingBet.setDateAndTimeCreated(ZonedDateTime.now().toString());

        //TODO LAGA floating point number
        double odds = pendingBet.getOddsSender();
        double likur = Math.floor((1 / odds) * 100 * 100) / 100;
        double oppOdds = Math.floor((1 / (100.0 - likur)) * 100 * 100) / 100;
        double oppAmount = (((pendingBet.getAmountSender() * odds) / oppOdds) * 100) /100;
        System.out.println("opp odds " + oppOdds + " opp amount " + oppAmount);
        System.out.println("your odds" + odds + " your amount " + pendingBet.getAmountSender());
        pendingBet.setOddsReceiver(oppOdds);
        pendingBet.setAmountReceiver(oppAmount);

        pendingBet.setAcceptSender(true);
        pendingBet.setAcceptReceiver(false);

        //add to senders pending bets
        Set<PendingBet> senderPendingBets = sender.getPendingBets();
        if(senderPendingBets == null){
            senderPendingBets = new HashSet<>();
        }
        senderPendingBets.add(pendingBet);
        pendingBet.setSender(sender.getUsername());
        pendingBet.setSenderID(sender.getId());

        //add to receiver pending bets
        Set<PendingBet> receiverPendingBets = receiver.getPendingBets();
        if(receiverPendingBets == null){
            receiverPendingBets = new HashSet<>();
        }
        receiverPendingBets.add(pendingBet);

        pendingBet.setReceiverID(receiver.getId());
        pendingBet.setReceiver(receiver.getUsername());



        pendingBetRepository.save(pendingBet);
    }

    @Override
    public void deletePendingBet(PendingBet pendingBet){
        pendingBetRepository.delete(pendingBet);
    }



    @Override
    public PendingBet findPendingBetById(Long Id){
        return pendingBetRepository.findOne(Id);
    }



    /*
    Bet service implementation
     */

    @Override
    public void deleteBet(Bet bet){
        betRepository.delete(bet);
    }

    //ma kannski eyda
    @Override
    public void saveBet(Bet bet){
        betRepository.save(bet);
    }

    @Override
    public void saveBet(PendingBet pendingBet, User currUser) throws Exception{
        User sender;
        User receiver;
        if(currUser.getUsername().equals(pendingBet.getSender())){
            sender = currUser;
            pendingBet.setAcceptSender(true);
            receiver = customUserDetailsService.findByUsername(pendingBet.getReceiver());
            receiver.removeCredit(pendingBet.getAmountReceiver());
        } else {
            receiver = currUser;
            pendingBet.setAcceptReceiver(true);
            sender = customUserDetailsService.findByUsername(pendingBet.getSender());
            sender.removeCredit(pendingBet.getAmountSender());
        }

        if(!pendingBet.isAcceptSender() || !pendingBet.isAcceptReceiver()){
            throw new Exception("Both users have to accept the bet.");
        }


        //athuga að sender á núþegar að hafa sent peninginn sinni...
        if(receiver.getCredit() < pendingBet.getAmountReceiver()){
            throw new Exception("Receiver does not have enough credits.");
        }

        if(sender.getCredit() < pendingBet.getAmountSender()){
            throw new Exception("Sender does not have enough credits");
        }




        receiver.removePendingBet(pendingBet);
        sender.removePendingBet(pendingBet);
        this.deletePendingBet(pendingBet);

        Bet newBet = new Bet(pendingBet);
        Set<Bet> senderBets = sender.getBets();
        if(senderBets == null){
            senderBets = new HashSet<>();
        }
        senderBets.add(newBet);

        Set<Bet> receiverBets = receiver.getBets();
        if(receiverBets == null){
            receiverBets = new HashSet<>();
        }
        receiverBets.add(newBet);

        this.saveBet(newBet);

    }

    @Override
    public Bet findBetById(Long id){
        return betRepository.findOne(id);
    }

}
