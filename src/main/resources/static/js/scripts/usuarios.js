const url = "/v1/usuarios";

function ajaxRequest(type, endpoint, data = null) {
	return $.ajax({
		type,
		url: endpoint,
		data: data ? JSON.stringify(data) : null,
		dataType: "json",
		contentType: data ? "application/json" : undefined,
		cache: false,
		timeout: 600000,
	});
}

function save(bandera) {
	const id = $("#guardar").data("id");
	const selectedRoles = $("#roles").val();
	const registro = {
		id,
		email: $("#email").val(),
		password: $("#password").val(),
		roles: selectedRoles
	};

	const type = bandera === 1 ? "POST" : "PUT";
	const endpoint = bandera === 1 ? url : `${url}/${id}`;

	ajaxRequest(type, endpoint, registro)
		.done((data) => {
			if (data.ok) {
				$("#modal-update").modal("hide");
				getTabla();
				$("#error-message").addClass("d-none");
				Swal.fire({
					icon: 'success',
					title: `Se ha ${bandera === 1 ? 'guardado' : 'actualizado'} el registro`,
					showConfirmButton: false,
					timer: 1500
				});
				clear();
			} else {
				showError(data.message);
			}
		}).fail(function(jqXHR) {
			let errorMessage = jqXHR.responseJSON && jqXHR.responseJSON.message ? jqXHR.responseJSON.message : "Error inesperado. Código: " + jqXHR.status;
			showError(errorMessage);
		});
}

function showError(message) {
	$("#error-message").text(message).removeClass("d-none");
}

function deactivate(id) {
	ajaxRequest("PUT", `${url}/${id}/deactivate`)
		.done((data) => {
			if (data.ok) {
				Swal.fire({
					icon: 'success',
					title: 'Se ha desactivado el registro',
					showConfirmButton: false,
					timer: 1500
				});
				getTabla();
			} else {
				Swal.fire({
					icon: 'error',
					title: 'Error',
					text: data.message,
					confirmButtonText: 'Aceptar'
				});
			}
		})
		.fail(handleError);
}
function activate(id) {
	ajaxRequest("PUT", `${url}/${id}/activate`)
		.done((data) => {
			if (data.ok) {
				Swal.fire({
					icon: 'success',
					title: 'Se ha activado el registro',
					showConfirmButton: false,
					timer: 1500
				});
				getTabla();
			} else {
				Swal.fire({
					icon: 'error',
					title: 'Error',
					text: data.message,
					confirmButtonText: 'Aceptar'
				});
			}
		})
		.fail(handleError);
}

function getTabla() {
	ajaxRequest("GET", url)
		.done((data) => {
			const t = $("#tablaRegistros").DataTable();
			t.clear().draw(false);

			if (data.ok) {
				$.each(data.body, (index, registro) => {
					const roles = registro.roles.map(role => role.nombre).join(', ');
					let botonera = `
                        <button type="button" class="btn btn-warning btn-xs editar">
                            <i class="fas fa-edit"></i>
                        </button>`;
					if (registro.activo) {
						botonera = botonera + ` <button type="button" class="btn btn-info btn-xs desactivar">
						                            <i class="fas fa-times"></i>
						                        </button>`;
					} else {
						botonera = botonera + ` <button type="button" class="btn btn-danger btn-xs activar">
												                            <i class="fas fa-check"></i>
												                        </button>`;
					}


					t.row.add([botonera, registro.id, registro.email, roles, registro.activo]);
				});
				t.draw(false);
			} else {
				console.error("Error en la respuesta: ", data.message);
			}
		})
		.fail(handleError);
}

function getFila(id) {
	ajaxRequest("GET", `${url}/${id}`)
		.done((data) => {
			if (data.ok) {
				
				$("#roles").val([]);
				const userRoles = data.body.roles;
                if (userRoles && userRoles.length > 0) {
                    const userRoleIds = userRoles.map(role => role.id);
                    $("#roles").val(userRoleIds);
                }
				
				
				$("#modal-title").text("Editar registro");
				$("#email").val(data.body.email);
				$("#guardar").data("id", data.body.id).data("bandera", 0);
				$("#modal-update").modal("show");
			} else {
				showError(data.message);
			}
		})
		.fail(handleError);
}

function clear() {
	$("#modal-title").text("Nuevo registro");
	$("#email").val("");
	$("#password").val("");
	$("#guardar").data("id", 0).data("bandera", 1);
	$("#roles").val([]); 
}

function handleError(jqXHR) {
	const errorMessage = jqXHR.responseJSON?.message || `Error inesperado. Código: ${jqXHR.status}`;
	Swal.fire({
		icon: 'error',
		title: 'Error',
		text: errorMessage,
		confirmButtonText: 'Aceptar'
	});
}

$(document).ready(function() {
	$("#tablaRegistros").DataTable({
		language: {
			lengthMenu: "Mostrar _MENU_ registros",
			zeroRecords: "No se encontraron coincidencias",
			info: "Mostrando del _START_ al _END_ de _TOTAL_ registros",
			infoEmpty: "Sin resultados",
			search: "Buscar: ",
			paginate: {
				first: "Primero",
				last: "Último",
				next: "Siguiente",
				previous: "Anterior",
			},
		},
		columnDefs: [
			{ targets: 0, orderable: false }
		],
	});

	clear();

	$("#nuevo").click(clear);

	$("#guardar").click(() => save($("#guardar").data("bandera")));

	$(document).on('click', '.desactivar', function() {
		const id = $(this).closest('tr').find('td:eq(1)').text();
		Swal.fire({
			title: 'Desactivar registro',
			text: "¿Está seguro de querer desactivar este registro?",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#3085d6',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Si'
		}).then((result) => {
			if (result.isConfirmed) {
				deactivate(id);
			}
		});
	});
	$(document).on('click', '.activar', function() {
			const id = $(this).closest('tr').find('td:eq(1)').text();
			Swal.fire({
				title: 'Activar registro',
				text: "¿Está seguro de querer activar este registro?",
				icon: 'warning',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Si'
			}).then((result) => {
				if (result.isConfirmed) {
					activate(id);
				}
			});
		});

	$(document).on('click', '.editar', function() {
		const id = $(this).closest('tr').find('td:eq(1)').text();
		getFila(id);
	});

	getTabla();

	$('#liSeguridad').addClass("menu-open");
	$('#liUsuario').addClass("active");

});