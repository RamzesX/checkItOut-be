package com.sm.instagram.instagramPlatform.influencer;

import com.sm.instagram.instagramPlatform.exceptions.ItemNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InfluencerService {
    @Autowired
    InfluencerRepository influencerRepository;

   public Optional<Influencer> findInfluencerById(Long id) {
      Optional<Influencer> influencer = influencerRepository.findById(id);
      if (influencer.isEmpty()) throw new ItemNotFoundException("Id not found", new Throwable("Id not found"));
      return influencer;
   }

    public List<Influencer> fetchCustomerDataAsList() {
        return influencerRepository.findAll();
    }

    public Page<Influencer> fetchCustomerDataAsPageWithFiltering(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return influencerRepository.findAll(pageable);
    }

    public Influencer createInfluencer(Influencer influencer) {
       return influencerRepository.save(influencer);
    }

    public Influencer updateInfluencer(Influencer influencer) {
        try {
            Influencer influencerToUpdate = influencerRepository.getReferenceById(influencer.getId());
            BeanUtils.copyProperties(influencer, influencerToUpdate);
            return influencerRepository.save(influencerToUpdate);
        } catch (Exception e) {
            throw new ItemNotFoundException("Influencer with given Id not found", e);
        }
    }

    public void deleteInfluencer(long id) {
        Optional<Influencer> influencer = influencerRepository.findById(id);
        if (influencer.isEmpty()) throw new ItemNotFoundException("Id not found", new Throwable("Id not found"));
        influencerRepository.deleteById(id);
    }
}
