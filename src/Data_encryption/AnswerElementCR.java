package data_encryption;

//import proposition_1_quicksort_optimise.*;
//import proposition_1_optimisee.*;
//import proposition_1_hash_function_byte.*;
//import propo1_pqt_tll_eg_vector2.*;
//import propo1_pqt_tll_eg_vector.*;
//import proposition1_equal_size_packets.*;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author smahboub
 */
public class AnswerElementCR {
    public String dataID;
    //public double PeriScoreinf;
    //public double periscoresup;
    public CryptedScore [] local_scores;
    public AnswerElementCR(int numc){
        local_scores=new CryptedScore[numc];
    }
 
    
}