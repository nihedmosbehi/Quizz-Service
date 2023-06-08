package quizz.service.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class question {
    private String question;
    private String[] responses;
    private String bonneReponses;
}
