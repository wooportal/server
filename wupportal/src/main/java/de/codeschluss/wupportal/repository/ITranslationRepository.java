package de.codeschluss.wupportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.codeschluss.wupportal.model.Translation;

public interface ITranslationRepository extends JpaRepository<Translation, String> {

}
