package data_encryption;


public class AnswerElementCR {
    public String dataID;
    //public double PeriScoreinf;
    //public double periscoresup;
    public CryptedScore [] local_scores;
    public AnswerElementCR(int numc){
        local_scores=new CryptedScore[numc];
    }
 
    
}
