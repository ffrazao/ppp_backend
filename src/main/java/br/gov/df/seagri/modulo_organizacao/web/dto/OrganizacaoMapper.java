package br.gov.df.seagri.modulo_organizacao.web.dto;

import org.mapstruct.Mapper;

import br.gov.df.seagri.dominio_central.web.BaseMapper;
import br.gov.df.seagri.modulo_organizacao.dominio.Organizacao;

// @Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Mapper(componentModel = "spring")
public interface OrganizacaoMapper extends BaseMapper<Organizacao, OrganizacaoRequestDTO, OrganizacaoResponseDTO> {
}
