<?php

class Recommend {
    
    public function similarityDistance($preference, $user1, $user2)                    //유사도 계산
    {
        $similar = array();
        $sum = 0;
    
        foreach($preference[$user1] as $key=>$value)
        {
            if(array_key_exists($key, $preference[$user2]))                              //공통된 키워드가 있으면 키워드 추출
                $similar[$key] = 1;
        }
        
        if(count($similar) == 0)                                                            //공통된 키워드가 없을시 0리턴
            return 0;
        
        foreach($preference[$user1] as $key=>$value)                                     
        {
            if(array_key_exists($key, $preference[$user2]))                              //공통된 키워드의 차이값의 제곱을 모두 더한다
                $sum = $sum + pow($value - $preference[$user2][$key], 2);
        }
        
        return  1/(1 + sqrt($sum));                                                         //유클리디안 유사도인 1/(1 + d)를 계산한 값
    }
    
    public function getRecommendations($preference, $targetUser)                               //추천 키워드 계산
    {
        $total = array();
        $simSum = array();
        $rank = array();
        $sim = 0;
        
        foreach($preference as $otherUser=>$values)                                      //반복문으로 다른 유저의 값과 비교
        {
            if($otherUser != $targetUser)                                                     //해당유저와 다른 유저일시에
            {
                $sim = $this->similarityDistance($preference, $targetUser, $otherUser);      //유사도 계산
            }
            
            if($sim > 0)                                                                    // 두 유저간의 유사도가 0이 아닐시
            {
                foreach($preference[$otherUser] as $key=>$value)
                {
                    if(!array_key_exists($key, $preference[$targetUser]))                      //유사도 계산했는지 체크
                    {
                        if(!array_key_exists($key, $total)) {                               //유저간 total값 초기화
                            $total[$key] = 0;
                        }
                        $total[$key] += $preference[$otherUser][$key] * $sim;            //유사도와 가중치인 관심도 곱해서 total값 계산
                        
                        if(!array_key_exists($key, $simSum)) {                             //유사도 합 초기화
                            $simSum[$key] = 0;
                        }
                        $simSum[$key] += $sim;                                             //유사도 합 계산
                    }
                }
                
            }
        }

        foreach($total as $key=>$value)
        {
            $rank[$key] = $value / $simSum[$key];                                         //랭크를 계산
        }
        
    array_multisort($rank, SORT_DESC);                                                     //소팅 후 가장 유사한 유저의 추천 키워드 리턴
  
    return $rank;      
    }
   
}

?>
