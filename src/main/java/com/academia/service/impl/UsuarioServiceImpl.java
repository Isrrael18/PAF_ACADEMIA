package com.academia.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.academia.entity.Rol;
import com.academia.entity.Usuario;
import com.academia.exception.GeneralException;
import com.academia.exception.NoDataFoundException;
import com.academia.exception.ValidateException;
import com.academia.repository.UsuarioRepository;
import com.academia.service.UsuarioService;
import com.academia.util.Encoder;
import com.academia.validator.UsuarioValidator;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private Encoder encoder;

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll(Pageable page) {
		try {
			List<Usuario> registros = repository.findAll(page).toList();
			return registros;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll() {
		try {
			List<Usuario> registros = repository.findAll();
			return registros;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByEmail(String email) {
		try {
			Usuario registro = repository.findByEmail(email);
			if (registro == null) {
				throw new NoDataFoundException("Error del servidor");
			}
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findById(int id) {
		try {
			Usuario registro = repository.findById(id)
					.orElseThrow(() -> new NoDataFoundException("No existe un registro con ese ID"));
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional
	public Usuario save(Usuario usuario) {
		try {

			UsuarioValidator.save(usuario);
			if (usuario.getId() == 0) {
				usuario.setActivo(true);
				usuario.setPassword(encoder.encodePassword(usuario.getPassword()));
				Set<Rol> roles = usuario.getRoles();
				
				//System.out.println(roles);
				if (roles != null && !roles.isEmpty()) {
		           usuario.setRoles(roles);
		        }
				
				Usuario nuevo = repository.save(usuario);				
				return nuevo;
			}
			Usuario registro = repository.findById(usuario.getId())
					.orElseThrow(() -> new NoDataFoundException("No existe un registro con ese ID"));
			registro.setEmail(usuario.getEmail());
			String pass=encoder.encodePassword(usuario.getPassword());
			registro.setPassword(pass);
			registro.setRoles(usuario.getRoles());
			repository.save(registro);
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional
	public Usuario deactivate(int id) {
		try {
			Usuario registro = repository.findById(id)
					.orElseThrow(() -> new NoDataFoundException("No existe un registro con ese ID"));
			registro.setActivo(false);
			repository.save(registro);
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}

	@Override
	@Transactional
	public Usuario activate(int id) {
		try {
			Usuario registro = repository.findById(id)
					.orElseThrow(() -> new NoDataFoundException("No existe un registro con ese ID"));
			registro.setActivo(true);
			repository.save(registro);
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		} catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
	}
}
